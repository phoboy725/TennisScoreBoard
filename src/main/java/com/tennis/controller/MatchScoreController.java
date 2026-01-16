package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.dto.MatchCurrentState;
import com.tennis.repository.PlayerRepository;
import com.tennis.service.MatchService;
import com.tennis.util.JSPUtil;
import com.tennis.util.RequestParamUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final MatchService matchService = ApplicationContext.matchService();
    private final PlayerRepository playerDao = ApplicationContext.playerRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UUID matchId = RequestParamUtil.getRequiredUuid(request, response, "uuid");
        if (matchId == null) {
            return;
        }

        MatchCurrentState currentMatch = matchService.findMatch(matchId);

        if (currentMatch == null) {
            response.sendRedirect(request.getContextPath() + "/matches");
            return;
        }

        String playerOneName = playerDao.findPlayerById(currentMatch.getPlayerOneId()).getName();
        String playerTwoName = playerDao.findPlayerById(currentMatch.getPlayerTwoId()).getName();

        if (!currentMatch.isMatchFinished()) {
            request.setAttribute("matchId", matchId.toString());
            request.setAttribute("currentMatch", currentMatch);
            request.setAttribute("playerOneName", playerOneName);
            request.setAttribute("playerTwoName", playerTwoName);
            request.getRequestDispatcher(JSPUtil.getJspPatch("match-score")).forward(request, response);
        } else {
            String winnerName = playerDao.findPlayerById(currentMatch.getWinnerId()).getName();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UUID matchId = RequestParamUtil.getRequiredUuid(request, response, "uuid");
        if (matchId == null) {
            return;
        }

        Integer scoreButtonId = RequestParamUtil.getRequiredInt(request, response, "scoreButtonId");
        if (scoreButtonId == null) {
            return;
        }

        matchService.updateScore(matchId, scoreButtonId);
        response.sendRedirect("/match-score?uuid=" + matchId);
    }
}
