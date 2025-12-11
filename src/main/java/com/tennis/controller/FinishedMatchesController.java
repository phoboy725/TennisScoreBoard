package com.tennis.controller;


import com.tennis.config.ApplicationContext;
import com.tennis.model.Match;
import com.tennis.repositories.MatchesDao;
import com.tennis.service.MatchService;
import com.tennis.util.JSPUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/matches")
public class FinishedMatchesController extends HttpServlet {

    private final MatchService matchService = ApplicationContext.matchService();
    private static final int PAGE_SIZE = 5;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String filterByPlayerName = request.getParameter("filter_by_player_name");
        String pageParam = request.getParameter("page");
        int currentPage = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }
        int offset = (currentPage - 1) * PAGE_SIZE;

        // 1) список матчей
        List<Match> matches = matchService.getMatches(filterByPlayerName, offset);

        // 2) общее количество матчей для пагинации
        int totalMatches = matchService.getTotalMatchesCount(filterByPlayerName);
        int noOfPages = (int) Math.ceil((double) totalMatches / PAGE_SIZE);

        // 3) атрибуты для JSP
        request.setAttribute("matches", matches);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("filter_by_player_name", filterByPlayerName != null ? filterByPlayerName : "");
        request.setAttribute("size", matches.size());

        // 4) форвард на JSP
        request.getRequestDispatcher("/WEB-INF/views/matches.jsp")
                .forward(request, response);
    }
}