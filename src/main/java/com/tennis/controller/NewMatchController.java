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

        String playerOneName = request.getParameter("playerOne");
        String playerTwoName = request.getParameter("playerTwo");

        List<String> checkPlayersNames = PlayerNamesValidation.check(playerOneName, playerTwoName);

        if (checkPlayersNames.isEmpty()) {
            Player playerOne = playerService.createPlayer(playerOneName);
            Player playerTwo = playerService.createPlayer(playerTwoName);
            String uuid = ongoingMatchService.createMatch(playerOne.getId(), playerTwo.getId()).toString();
            redirectTo(ViewsPath.MATCH_SCORE.jsp(), Map.of("uuid", uuid), request, response);
        } else {
            request.setAttribute("playerOne", playerOneName);
            request.setAttribute("playerTwo", playerTwoName);
            request.setAttribute("errors", checkPlayersNames);
            forwardTo(ViewsPath.NEW_MATCH.jsp(), request, response);

        }
    }
}
