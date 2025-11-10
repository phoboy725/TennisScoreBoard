package com.tennis.controller;

import com.tennis.model.Player;
import com.tennis.repositories.PlayerDao;
import com.tennis.service.MatchService;
import com.tennis.service.PlayerService;
import com.tennis.util.JSPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    private PlayerDao playerDao = PlayerDao.getInstance();
    private MatchService matchService = MatchService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher(JSPUtil.getJspPatch("new-match")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String playerOneName = request.getParameter("playerOne");
        String playerTwoName = request.getParameter("playerTwo");

        if (playerOneName.equalsIgnoreCase(playerTwoName)) {
            request.setAttribute("errorMessage", "Имена игроков должны быть уникальными");
            request.getRequestDispatcher(JSPUtil.getJspPatch("new-match")).forward(request, response);
            return;
        } else {
            PlayerService playerService = new PlayerService(playerDao);
            Player playerOne = playerService.getOrCreatePlayer(playerOneName);
            Player playerTwo = playerService.getOrCreatePlayer(playerTwoName);
            String matchId = matchService.createMatch(playerOne.getId(), playerTwo.getId()).toString();
            response.sendRedirect("/match-score?uuid=" + matchId);

        }
    }
}
