package com.tennis.repositories;

import com.tennis.model.Match;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class MatchesDao {

    private final EntityManagerFactory entityManagerFactory;

    public MatchesDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Match> readAll(int offset) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Match> matches = entityManager.createQuery("SELECT m FROM Match m " +
                                                            "JOIN FETCH m.playerOne  " +
                                                            "JOIN FETCH m.playerTwo  " +
                                                            "JOIN FETCH m.winner").setFirstResult(offset).setMaxResults(5).getResultList();
        entityManager.close();
        return matches;
    }

    public List<Match> findByPlayerName(String playerNameFilter, int offset) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Match> matches = entityManager.createQuery("SELECT m FROM Match m " +
                "JOIN FETCH m.playerOne  " +
                "JOIN FETCH m.playerTwo  " +
                "JOIN FETCH m.winner " +
                        "WHERE LOWER(m.playerOne.name) LIKE LOWER(:name)" +
                        "OR LOWER(m.playerTwo.name) LIKE LOWER(:name) " +
                        "OR LOWER(m.winner.name) LIKE LOWER(:name)",
                Match.class).setParameter("name", "%" + playerNameFilter + "%").
                            setFirstResult(offset).
                            setMaxResults(5).
                            getResultList();
        entityManager.close();
        return matches;
    }

    public int countByPlayerName(String playerNameFilter) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Match> matches = entityManager.createQuery("SELECT m FROM Match m " +
                                "JOIN FETCH m.playerOne  " +
                                "JOIN FETCH m.playerTwo  " +
                                "JOIN FETCH m.winner " +
                                "WHERE LOWER(m.playerOne.name) LIKE LOWER(:name)" +
                                "OR LOWER(m.playerTwo.name) LIKE LOWER(:name) " +
                                "OR LOWER(m.winner.name) LIKE LOWER(:name)",
                        Match.class).setParameter("name", "%" + playerNameFilter + "%").
                getResultList();
        entityManager.close();
        return matches.size();
    }

    public int countAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Match> matches = entityManager.createQuery("SELECT m FROM Match m " +
                                "JOIN FETCH m.playerOne  " +
                                "JOIN FETCH m.playerTwo  " +
                                "JOIN FETCH m.winner ", Match.class).
                getResultList();
        entityManager.close();
        return matches.size();
    }

    public void saveFinishedMatch(Match match) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(match);
            entityTransaction.commit();
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
