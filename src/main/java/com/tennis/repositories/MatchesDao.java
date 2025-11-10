package com.tennis.repositories;

import com.tennis.model.Match;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class MatchesDao {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Tennis");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    public List<Match> readAll() {
        List<Match> matches = entityManager.createQuery("SELECT m FROM match m " +
                                                            "JOIN FETCH m.playerOne  " +
                                                            "JOIN FETCH m.playerTwo  " +
                                                            "JOIN FETCH m.winner").getResultList();
        entityManager.close();
        return matches;
    }
}
