package com.tennis.controller;


import com.tennis.model.Match;
import com.tennis.repositories.MatchesDao;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        MatchesDao matchesDAO = new MatchesDao();
        request.getParameter("action");
        List<Match> matches = matchesDAO.readAll();
        for (Match m: matches) {
            System.out.println(m);
        }
        request.setAttribute("matches", matches);
        request.getRequestDispatcher(JSPUtil.getJspPatch("matches")).forward(request, response);

    }
}