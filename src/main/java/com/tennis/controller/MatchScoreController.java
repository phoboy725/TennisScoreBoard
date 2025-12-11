package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.dto.MatchCurrentState;
import com.tennis.model.Player;
import com.tennis.repositories.PlayerDao;
import com.tennis.service.MatchService;
import com.tennis.util.JSPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final MatchService matchService = ApplicationContext.matchService();
    private final PlayerDao playerDao = ApplicationContext.playerDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UUID matchId = UUID.fromString(request.getParameter("uuid"));
        MatchCurrentState currentMatch = matchService.getMatch(matchId);
        String playerOneName = playerDao.getPlayerById(currentMatch.getPlayerOneId()).getName();
        String playerTwoName = playerDao.getPlayerById(currentMatch.getPlayerTwoId()).getName();

        if (!currentMatch.isMatchFinished()) {
            request.setAttribute("matchId", matchId.toString());
            request.setAttribute("currentMatch", currentMatch);
            request.setAttribute("playerOneName", playerOneName);
            request.setAttribute("playerTwoName", playerTwoName);
            request.getRequestDispatcher(JSPUtil.getJspPatch("match-score")).forward(request, response);
        } else {
            String winnerName = playerDao.getPlayerById(currentMatch.getWinnerId()).getName();
            request.setAttribute("matchId", matchId.toString());
            request.setAttribute("currentMatch", currentMatch);
            request.setAttribute("winnerName", winnerName);
            request.setAttribute("playerOneName", playerOneName);
            request.setAttribute("playerTwoName", playerTwoName);
            request.getRequestDispatcher(JSPUtil.getJspPatch("match-winner")).forward(request, response);
            matchService.finishMatch(matchId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UUID matchId = UUID.fromString(request.getParameter("uuid"));
        Integer scoreButtonId = Integer.parseInt(request.getParameter("scoreButtonId"));
        matchService.updateScore(matchId, scoreButtonId);
        response.sendRedirect("/match-score?uuid=" + matchId);

    }
}
