package com.tennis.config;

import com.tennis.repositories.MatchesDao;
import com.tennis.repositories.PlayerDao;
import com.tennis.service.MatchService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ApplicationContext {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Tennis");

    private static final PlayerDao PLAYER_DAO   = new PlayerDao(entityManagerFactory);
    private static final MatchesDao MATCHES_DAO = new MatchesDao(entityManagerFactory);

    private static final MatchService MATCH_SERVICE =
            new MatchService(MATCHES_DAO, PLAYER_DAO);

    private ApplicationContext() {}

    public static MatchService matchService() {
        return MATCH_SERVICE;
    }

    public static PlayerDao playerDao() {
        return PLAYER_DAO;
    }
}
