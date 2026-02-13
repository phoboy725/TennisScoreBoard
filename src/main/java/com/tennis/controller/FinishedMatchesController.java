package com.tennis.controller;


import com.tennis.config.ApplicationContext;
import com.tennis.dto.FinishedMatchDto;
import com.tennis.dto.MatchDtoFactory;
import com.tennis.entity.Match;
import com.tennis.service.FinishedMatchService;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/matches")
public class FinishedMatchesController extends BaseController {

    private static final int PAGE_SIZE = 5;
    private static final String MATCHES_ATTRIBUTE = "matches";
    private static final String CURRENT_PAGE_ATTRIBUTE = "currentPage";
    private static final String TOTAL_PAGES_ATTRIBUTE = "totalPages";
    private static final String FILTER_ATTRIBUTE = "filter_by_player_name";
    private static final String SIZE_PARAM = "size";
    private static final String PAGE_PARAM = "page";
    private static final String FILTER_PARAM = "filter_by_player_name";
    private FinishedMatchService finishedMatchService;
    private OngoingMatchService ongoingMatchService;
    private PlayerService playerService;
    private MatchDtoFactory matchDtoFactory;


    @Override
    public void init() {
        this.finishedMatchService = ApplicationContext.finishedMatchService();
        this.ongoingMatchService = ApplicationContext.ongoingMatchService();
        this.playerService = ApplicationContext.playerService();
        this.matchDtoFactory = new MatchDtoFactory(ongoingMatchService, playerService);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String filterByPlayerName = request.getParameter(FILTER_PARAM);
        String pageParam = request.getParameter(PAGE_PARAM);
        int currentPage = getCurrentPage(pageParam);
        int offset = (currentPage - 1) * PAGE_SIZE;
        int limit = PAGE_SIZE;

        List<Match> matches = finishedMatchService.getMatches(filterByPlayerName, offset, limit);
        List<FinishedMatchDto> matchesDto = matches.stream().map(matchDtoFactory::from).toList();
        Long totalMatches = finishedMatchService.getTotalMatchesCount(filterByPlayerName);
        int totalPages = (int) Math.ceil((double) totalMatches / PAGE_SIZE);

        request.setAttribute(MATCHES_ATTRIBUTE, matchesDto);
        request.setAttribute(CURRENT_PAGE_ATTRIBUTE, currentPage);
        request.setAttribute(TOTAL_PAGES_ATTRIBUTE, totalPages);
        request.setAttribute(FILTER_ATTRIBUTE, filterByPlayerName != null ? filterByPlayerName : "");
        request.setAttribute(SIZE_PARAM, matchesDto.size());
        forwardTo(ViewsPath.MATCHES.jsp(), request, response);
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