package com.tennis.config;

import com.tennis.persistence.JpaMatchesRepository;
import com.tennis.persistence.JpaPlayerRepository;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;
import com.tennis.service.MatchScoreService;
import com.tennis.service.FinishedMatchService;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;

public class ApplicationContext {

    private static final PlayerRepository PLAYER_REPOSITORY = new JpaPlayerRepository();
    private static final MatchesRepository MATCHES_REPOSITORY = new JpaMatchesRepository();
    private static final MatchScoreService SCORE = new MatchScoreService();
    private static final FinishedMatchService FINISHED_MATCH_SERVICE =
            new FinishedMatchService(MATCHES_REPOSITORY, PLAYER_REPOSITORY, SCORE);
    private static final OngoingMatchService ONGOING_MATCH_SERVICE =
            new OngoingMatchService(MATCHES_REPOSITORY, PLAYER_REPOSITORY, SCORE);
    private static final PlayerService PLAYER_SERVICE =
            new PlayerService(PLAYER_REPOSITORY);

    private ApplicationContext() {}

    public static FinishedMatchService finishedMatchService() {
        return FINISHED_MATCH_SERVICE;
    }

    public static OngoingMatchService ongoingMatchService() {
        return ONGOING_MATCH_SERVICE;
    }

    public static PlayerService playerService() {
        return PLAYER_SERVICE;
    }

    public static PlayerRepository playerRepository() {
        return PLAYER_REPOSITORY;
    }

}
