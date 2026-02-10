package com.tennis.controller;


import com.tennis.config.ApplicationContext;
import com.tennis.entity.Match;
import com.tennis.service.FinishedMatchService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/matches")
public class FinishedMatchesController extends HttpServlet {

    private static final int PAGE_SIZE = 5;
    private FinishedMatchService finishedMatchService;


    @Override
    public void init() {
        this.finishedMatchService = ApplicationContext.finishedMatchService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String filterByPlayerName = request.getParameter("filterByPlayerName");
        String pageParam = request.getParameter("page");
        int currentPage = getCurrentPage(pageParam);
        int offset = (currentPage - 1) * PAGE_SIZE;
        int limit = PAGE_SIZE;

        List<Match> matches = finishedMatchService.getMatches(filterByPlayerName, offset, limit);
        Long totalMatches = finishedMatchService.getTotalMatchesCount(filterByPlayerName);
        int totalPages = (int) Math.ceil((double) totalMatches / PAGE_SIZE);

        request.setAttribute("matches", matches);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("filterByPlayerName", filterByPlayerName != null ? filterByPlayerName : "");
        request.setAttribute("size", matches.size());
        request.getRequestDispatcher("/WEB-INF/views/matches.jsp").forward(request, response);
    }

    private static int getCurrentPage(String pageParam) {
        if (pageParam != null && !pageParam.isBlank()) {
            try {
                return Math.max(Integer.parseInt(pageParam), 1);
            } catch (NumberFormatException ignored) {
            }
        }
        return 1;
    }
}