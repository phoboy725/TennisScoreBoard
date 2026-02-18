package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScored;
import com.tennis.dto.MatchDtoFactory;
import com.tennis.dto.MatchUpdateDto;
import com.tennis.dto.OngoingMatchDto;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.service.FinishedMatchService;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends BaseController {

    private static final String MATCH_ATTRIBUTE = "ongoingMatch";
    private static final String UUID_ATTRIBUTE = "uuid";
    private static final String PLAYER_SCORED_PARAM = "playerScored";
    private static final String UUID_PARAM = "uuid";
    private static final String WINNER_SNAPSHOT = "winnerSnapshot";
    private PlayerService playerService;
    private OngoingMatchService ongoingMatchService;
    private FinishedMatchService finishedMatchService;
    private MatchDtoFactory matchDtoFactory;

    @Override
    public void init() {
        this.ongoingMatchService = ApplicationContext.ongoingMatchService();
        this.finishedMatchService = ApplicationContext.finishedMatchService();
        this.playerService = ApplicationContext.playerService();
        this.matchDtoFactory = new MatchDtoFactory(ongoingMatchService, finishedMatchService, playerService);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UUID uuid = UUID.fromString(getRequiredParameter(request, UUID_PARAM));
        Optional<OngoingMatch> ongoingMatch = ongoingMatchService.findMatch(uuid);

        if (ongoingMatch.isEmpty()) {
            redirectToNewMatchWithMessage(request, response);
            return;
        }

        OngoingMatchDto ongoingMatchDto = matchDtoFactory.fromMatch(ongoingMatch.get());
        request.setAttribute(MATCH_ATTRIBUTE, ongoingMatchDto);
        request.setAttribute(UUID_ATTRIBUTE, uuid);
        forwardTo(ViewsPath.MATCH_SCORE.jsp(), request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        UUID uuid = UUID.fromString(getRequiredParameter(request, UUID_PARAM));
        PlayerScored playerScored = PlayerScored.from(getRequiredParameter(request, PLAYER_SCORED_PARAM));

        try {

            MatchUpdateDto updateDto = ongoingMatchService.updateScoreAndFinishMatchIfNeeded(uuid, playerScored);

            if (updateDto.finished()) {

                OngoingMatchDto finishedMatchSnapshot = matchDtoFactory.fromMatch(updateDto.ongoingMatch());
                request.getSession().setAttribute(WINNER_SNAPSHOT, finishedMatchSnapshot);
                redirectTo(ViewsPath.MATCH_WINNER.jsp(), Map.of(), request, response);
                return;
            }

            OngoingMatchDto ongoingMatchDto = matchDtoFactory.fromMatch(updateDto.ongoingMatch());
            request.setAttribute(MATCH_ATTRIBUTE, ongoingMatchDto);
            redirectTo(ViewsPath.MATCH_SCORE.jsp(), Map.of(UUID_ATTRIBUTE, uuid), request, response);

        } catch (MatchNotFoundException e) {

            redirectTo(ViewsPath.MATCH_WINNER.jsp(), Map.of(), request, response);
        }

    }

}
