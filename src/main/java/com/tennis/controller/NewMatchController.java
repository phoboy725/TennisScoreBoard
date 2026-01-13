package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.entity.Player;
import com.tennis.repositories.PlayerDao;
import com.tennis.service.MatchService;
import com.tennis.service.PlayerService;
import com.tennis.util.JSPUtil;
import com.tennis.validation.PlayerNamesValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    private final PlayerDao playerDao = ApplicationContext.playerDao();
    private final MatchService matchService = ApplicationContext.matchService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSPUtil.getJspPatch("new-match")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String playerOneName = request.getParameter("playerOne");
        String playerTwoName = request.getParameter("playerTwo");

        String checkPlayersNames = PlayerNamesValidation.check(playerOneName, playerTwoName);

        if (checkPlayersNames == null) {
            PlayerService playerService = new PlayerService(playerDao);
            Player playerOne = playerService.getOrCreatePlayer(playerOneName);
            Player playerTwo = playerService.getOrCreatePlayer(playerTwoName);
            String matchId = matchService.createMatch(playerOne.getId(), playerTwo.getId()).toString();
            response.sendRedirect("/match-score?uuid=" + matchId);
        } else {
            request.setAttribute("errorMessage", checkPlayersNames);
            request.getRequestDispatcher(JSPUtil.getJspPatch("new-match")).forward(request, response);
        }
    }
}
