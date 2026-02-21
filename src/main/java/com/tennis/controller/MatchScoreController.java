package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScored;
import com.tennis.dto.MatchDtoFactory;
import com.tennis.dto.MatchUpdateDto;
import com.tennis.dto.OngoingMatchDto;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.exception.MissingParameterException;
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
    private static final String FINISHED_MATCH_ATTRIBUTE = "finishedMatch";
    private static final String PLAYER_SCORED_PARAM = "playerScored";
    private static final String UUID_PARAM = "uuid";
    private static final String NOT_EXIST = "Match doesn't exist - please create a new one";
    private static final String WRONG_UUID = "Error in UUID - please create a new match";
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

        try {

            UUID uuid = getUuidParameter(request, UUID_PARAM);
            OngoingMatch ongoingMatch = ongoingMatchService.getMatchOrThrow(uuid);

            OngoingMatchDto ongoingMatchDto = matchDtoFactory.fromMatch(ongoingMatch);
            request.setAttribute(MATCH_ATTRIBUTE, ongoingMatchDto);
            request.setAttribute(UUID_ATTRIBUTE, uuid);
            forwardTo(ViewsPath.MATCH_SCORE.jsp(), request, response);

        } catch (MissingParameterException e) {
            errorRedirect(ViewsPath.NEW_MATCH.jsp(), WRONG_UUID, request, response);
        } catch (MatchNotFoundException e) {
            errorRedirect(ViewsPath.NEW_MATCH.jsp(), NOT_EXIST, request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {

            UUID uuid = getUuidParameter(request, UUID_PARAM);
            PlayerScored playerScored = PlayerScored.from(getRequiredParameter(request, PLAYER_SCORED_PARAM));

            MatchUpdateDto updateDto = ongoingMatchService.updateScoreAndFinishMatchIfNeeded(uuid, playerScored);
            OngoingMatchDto ongoingMatchDto = matchDtoFactory.fromMatch(updateDto.ongoingMatch());

            if (updateDto.ongoingMatch().isFinished()) {

                request.getSession().setAttribute(FINISHED_MATCH_ATTRIBUTE, ongoingMatchDto);
                redirectTo(ViewsPath.MATCH_WINNER.jsp(), Map.of(), request, response);

            } else {

                request.setAttribute(MATCH_ATTRIBUTE, ongoingMatchDto);
                redirectTo(ViewsPath.MATCH_SCORE.jsp(), Map.of(UUID_ATTRIBUTE, uuid), request, response);

            }

        } catch (MissingParameterException e) {
            errorRedirect(ViewsPath.NEW_MATCH.jsp(), WRONG_UUID, request, response);
        } catch (MatchNotFoundException e) {
            errorRedirect(ViewsPath.NEW_MATCH.jsp(), NOT_EXIST, request, response);
        }
    }
}
