package com.tennis.controller;

import com.tennis.config.ApplicationContext;
import com.tennis.dto.MatchDtoFactory;
import com.tennis.dto.OngoingMatchDto;
import com.tennis.exception.MissingParameterException;
import com.tennis.service.FinishedMatchService;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/match-winner")
public class MatchWinnerController extends BaseController {

    private static final String FINISHED_MATCH_ATTRIBUTE = "finishedMatch";
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

        OngoingMatchDto finishedMatchDto;

        try {

            finishedMatchDto = (OngoingMatchDto) request.getSession().getAttribute(FINISHED_MATCH_ATTRIBUTE);

        } catch (MissingParameterException e) {
            throw new MissingParameterException("Missing parameter " + e);
        }

        request.setAttribute(FINISHED_MATCH_ATTRIBUTE, finishedMatchDto);
        forwardTo(ViewsPath.MATCH_WINNER.jsp(), request, response);

        request.getSession().removeAttribute(WINNER_SNAPSHOT);

    }
}

