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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends BaseController {

    private static final String MATCH_ATTRIBUTE = "ongoingMatch";
    private static final String UUID_ATTRIBUTE = "uuid";
    private static final String PLAYER_SCORED_PARAM = "playerScored";
    private static final String UUID_PARAM = "uuid";
    private PlayerService playerService;
    private OngoingMatchService ongoingMatchService;
    private MatchDtoFactory matchDtoFactory;


    @Override
    public void init() {
        this.ongoingMatchService = ApplicationContext.ongoingMatchService();
        this.playerService = ApplicationContext.playerService();
        this.matchDtoFactory = new MatchDtoFactory(ongoingMatchService, playerService);
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

        UUID uuid = UUID.fromString(getRequiredParameter(request, UUID_PARAM));
        OngoingMatch ongoingMatch = ongoingMatchService.findMatch(uuid);

        if (ongoingMatch == null) {

            redirectTo(ViewsPath.MATCHES.jsp(), Map.of(), request, response);
            return;
        }

        MatchResponseDto matchResponseDto = matchDtoFactory.fromMatch(uuid);
        request.setAttribute(MATCH_ATTRIBUTE, matchResponseDto);
        request.setAttribute(UUID_ATTRIBUTE, uuid);
        forwardTo(ViewsPath.MATCH_SCORE.jsp(), request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        UUID uuid = UUID.fromString(getRequiredParameter(request, UUID_PARAM));
        PlayerScored playerScored = PlayerScored.from(getRequiredParameter(request, PLAYER_SCORED_PARAM));
        OngoingMatch ongoingMatch = ongoingMatchService.findMatch(uuid);

        try {
            ongoingMatchService.updateScore(uuid, playerScored);

            if (ongoingMatch == null) {
                redirectTo(ViewsPath.MATCHES.jsp(), Map.of(), request, response);
                return;
            }

            if (ongoingMatch.isFinished()) {

                MatchResponseDto matchResponseDto = matchDtoFactory.fromMatch(uuid);
                request.setAttribute(MATCH_ATTRIBUTE, matchResponseDto);
                forwardTo(ViewsPath.MATCH_WINNER.jsp(), request, response);
                ongoingMatchService.finishMatch(uuid);
                return;
            }

            MatchResponseDto matchResponseDto = matchDtoFactory.fromMatch(uuid);
            request.setAttribute(MATCH_ATTRIBUTE, matchResponseDto);
            redirectTo(ViewsPath.MATCH_SCORE.jsp(), Map.of(UUID_ATTRIBUTE, uuid), request, response);

        } catch (MatchNotFoundException e) {
            redirectTo(ViewsPath.MATCHES.jsp(), Map.of(), request, response);
        }

    }

}
