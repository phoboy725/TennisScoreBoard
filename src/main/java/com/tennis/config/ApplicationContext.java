package com.tennis.config;

import com.tennis.persistence.JpaMatchesRepository;
import com.tennis.persistence.JpaPlayerRepository;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;
import com.tennis.service.MatchScoreService;
import com.tennis.service.MatchService;

public class ApplicationContext {

    private static final PlayerRepository PLAYER_REPOSITORY = new JpaPlayerRepository();
    private static final MatchesRepository MATCHES_REPOSITORY = new JpaMatchesRepository();
    private static final MatchScoreService SCORE = new MatchScoreService();

    private static final MatchService MATCH_SERVICE =
            new MatchService(MATCHES_REPOSITORY, PLAYER_REPOSITORY, SCORE);

    private ApplicationContext() {}

    public static MatchService matchService() {
        return MATCH_SERVICE;
    }

    public static PlayerRepository playerRepository() {
        return PLAYER_REPOSITORY;
    }

}
