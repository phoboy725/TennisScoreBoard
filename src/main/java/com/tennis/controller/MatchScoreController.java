package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScored;
import com.tennis.dto.MatchDtoFactory;
import com.tennis.dto.MatchResponseDto;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.exception.MissingParameterException;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;
import com.tennis.util.JSPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends BaseController {

    private PlayerService playerService;
    private OngoingMatchService ongoingMatchService;
    private MatchDtoFactory matchDtoFactory;
    private final String MATCH = "ongoingMatch";

    @Override
    public void init() {
        this.playerService = ApplicationContext.playerService();
        this.ongoingMatchService = ApplicationContext.ongoingMatchService();
        this.matchDtoFactory = new MatchDtoFactory();
    }

    private static String getRequiredParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value == null || value.isBlank()) {
            throw new MissingParameterException("Missing " + paramName);
        }
        return value;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UUID uuid = UUID.fromString(getRequiredParameter(request, "uuid"));

        OngoingMatch ongoingMatch = ongoingMatchService.findMatch(uuid);

        if (ongoingMatch == null) {
            response.sendRedirect(request.getContextPath() + "/matches");
            return;
        }

        MatchResponseDto matchResponseDto = matchDtoFactory.fromMatch(uuid);

        request.setAttribute(MATCH, matchResponseDto);
        forwardTo(ViewsPath.MATCH_SCORE.jsp(), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UUID uuid = UUID.fromString(getRequiredParameter(request, "uuid"));
        PlayerScored scoreButtonId = PlayerScored.from(getRequiredParameter(request, "scoreButtonId"));
        OngoingMatch ongoingMatch = ongoingMatchService.findMatch(uuid);
        String playerOneName = playerService.findPlayerById(ongoingMatch.getPlayerOneScore().getId()).getName();
        String playerTwoName = playerService.findPlayerById(ongoingMatch.getPlayerTwoScore().getId()).getName();

        try {
            ongoingMatchService.updateScore(uuid, scoreButtonId);

            if (ongoingMatch == null) {
                redirectTo(ViewsPath.MATCHES.jsp(), Map.of(), request, response);
                return;
            }

            if (ongoingMatch.isFinished()) {

                MatchResponseDto matchResponseDto = matchDtoFactory.fromMatch(uuid);
                request.setAttribute(MATCH, matchResponseDto);

                ongoingMatchService.finishMatch(uuid);
//                String winnerName = playerService.findPlayerById(ongoingMatch.getWinnerId()).getName();
//                request.setAttribute("matchId", uuid.toString());
//                request.setAttribute("currentMatch", ongoingMatch);
//                request.setAttribute("winnerName", winnerName);
//                request.setAttribute("playerOneName", playerOneName);
//                request.setAttribute("playerTwoName", playerTwoName);
//                request.getRequestDispatcher(JSPUtil.getJspPath("match-winner")).forward(request, response);
                forwardTo(ViewsPath.MATCH_WINNER.jsp(), request, response);
                return;
            }

            MatchResponseDto matchResponseDto = matchDtoFactory.fromMatch(uuid);

            request.setAttribute(MATCH, matchResponseDto);

            response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuid);

        } catch (MatchNotFoundException e) {
            response.sendRedirect(request.getContextPath() + "/matches");
        }
    }

}
