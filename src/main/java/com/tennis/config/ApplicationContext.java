package com.tennis.config;

import com.tennis.persistence.JpaMatchesRepository;
import com.tennis.persistence.JpaPlayerRepository;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;
import com.tennis.service.MatchService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ApplicationContext {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Tennis");

    private static final PlayerRepository PLAYER_DAO   = new JpaPlayerRepository(entityManagerFactory);
    private static final MatchesRepository MATCHES_DAO = new JpaMatchesRepository(entityManagerFactory);

    private static final MatchService MATCH_SERVICE =
            new MatchService(MATCHES_DAO, PLAYER_DAO);

    private ApplicationContext() {}

    public static MatchService matchService() {
        return MATCH_SERVICE;
    }

    public static PlayerRepository playerDao() {
        return PLAYER_DAO;
    }
}
