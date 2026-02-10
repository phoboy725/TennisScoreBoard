package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScored;
import com.tennis.dto.MatchCurrentState;
import com.tennis.dto.MatchDto;
import com.tennis.dto.MatchScorePageDto;
import com.tennis.dto.MatchStateDto;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.exception.MissingParameterException;
import com.tennis.mapper.MatchScorePageMapper;
import com.tennis.mapper.MatchStateMapper;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;
import com.tennis.util.JSPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends BaseController {

    private PlayerService playerService;
    private OngoingMatchService ongoingMatchService;
    private final String MATCH = "match";

    @Override
    public void init() {
        this.playerService = ApplicationContext.playerService();
        this.ongoingMatchService = ApplicationContext.ongoingMatchService();
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

        String playerOneName = playerService.findPlayerById(ongoingMatch.getPlayerOneScoreView().getId()).getName();
        String playerTwoName = playerService.findPlayerById(ongoingMatch.getPlayerTwoScoreView().getId()).getName();

        MatchScorePageDto page = MatchScorePageMapper.toPageDto(
                uuid.toString(),
                playerOneName,
                playerTwoName,
                ongoingMatch
        );

        request.setAttribute(MATCH, page);
        forwardTo(ViewsPath.MATCH_SCORE.jsp(), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UUID uuid = UUID.fromString(getRequiredParameter(request, "uuid"));
        PlayerScored scoreButtonId = PlayerScored.from(getRequiredParameter(request, "scoreButtonId"));
        OngoingMatch ongoingMatch = ongoingMatchService.findMatch(uuid);
        String playerOneName = playerService.findPlayerById(ongoingMatch.getPlayerOneScoreView().getId()).getName();
        String playerTwoName = playerService.findPlayerById(ongoingMatch.getPlayerTwoScoreView().getId()).getName();

        try {
            ongoingMatchService.updateScore(uuid, scoreButtonId);
            OngoingMatch match = ongoingMatchService.findMatch(uuid);

            if (match == null) {
                redirectTo(ViewsPath.MATCHES.jsp(), Map.of(), request, response);
                return;
            }

            if (match.isFinished()) {
                ongoingMatchService.finishMatch(uuid);
                String winnerName = playerService.findPlayerById(match.getWinnerId()).getName();
                request.setAttribute("matchId", uuid.toString());
                request.setAttribute("currentMatch", match);
                request.setAttribute("winnerName", winnerName);
                request.setAttribute("playerOneName", playerOneName);
                request.setAttribute("playerTwoName", playerTwoName);
                request.getRequestDispatcher(JSPUtil.getJspPath("match-winner")).forward(request, response);
                forwardTo(ViewsPath.MATCH_WINNER.jsp(), request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuid);

        } catch (MatchNotFoundException e) {
            response.sendRedirect(request.getContextPath() + "/matches");
        }
    }

}
