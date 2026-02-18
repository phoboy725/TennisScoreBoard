package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.entity.Player;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;
import com.tennis.validation.PlayerNamesValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/new-match")
public class NewMatchController extends BaseController {

    private static final String PLAYER_ONE_ATTRIBUTE = "playerOne";
    private static final String PLAYER_TWO_ATTRIBUTE = "playerTwo";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String UUID_ATTRIBUTE = "uuid";
    private static final String PLAYER_ONE_PARAM = "playerOne";
    private static final String PLAYER_TWO_PARAM = "playerTwo";
    private PlayerService playerService;
    private OngoingMatchService ongoingMatchService;

    @Override
    public void init() {
        this.playerService = ApplicationContext.playerService();
        this.ongoingMatchService = ApplicationContext.ongoingMatchService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardTo(ViewsPath.NEW_MATCH.jsp(), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String playerOneName = request.getParameter(PLAYER_ONE_PARAM);
        String playerTwoName = request.getParameter(PLAYER_TWO_PARAM);

        List<String> checkPlayersNames = PlayerNamesValidation.check(playerOneName, playerTwoName);

        if (checkPlayersNames.isEmpty()) {
            Player playerOne = playerService.getOrCreatePlayer(playerOneName);
            Player playerTwo = playerService.getOrCreatePlayer(playerTwoName);
            String uuid = ongoingMatchService.createMatch(playerOne.getId(), playerTwo.getId()).toString();
            redirectTo(ViewsPath.MATCH_SCORE.jsp(), Map.of(UUID_ATTRIBUTE, uuid), request, response);
        } else {
            request.setAttribute(PLAYER_ONE_ATTRIBUTE, playerOneName);
            request.setAttribute(PLAYER_TWO_ATTRIBUTE, playerTwoName);
            request.setAttribute(ERRORS_ATTRIBUTE, checkPlayersNames);
            forwardTo(ViewsPath.NEW_MATCH.jsp(), request, response);

        }
    }
}
