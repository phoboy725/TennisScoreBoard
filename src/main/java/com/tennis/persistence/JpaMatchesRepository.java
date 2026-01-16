package com.tennis.persistence;

import com.tennis.exception.DatabaseException;
import com.tennis.entity.Match;
import com.tennis.repository.MatchesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class JpaMatchesRepository implements MatchesRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaMatchesRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Match> readAll(int offset) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Match> matches = entityManager.createQuery("SELECT m FROM Match m " +
                                                            "JOIN FETCH m.playerOne  " +
                                                            "JOIN FETCH m.playerTwo  " +
                                                            "JOIN FETCH m.winner " +
                                                            "ORDER BY m.id DESC")
                                                            .setFirstResult(offset)
                                                            .setMaxResults(5)
                                                            .getResultList();
        entityManager.close();
        System.out.println(matches);
        return matches;
    }

    @Override
    public List<Match> findMatchesByPlayerName(String playerNameFilter, int offset) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Match> matches = entityManager.createQuery("SELECT m FROM Match m " +
                "JOIN FETCH m.playerOne  " +
                "JOIN FETCH m.playerTwo  " +
                "JOIN FETCH m.winner " +
                        "WHERE LOWER(m.playerOne.name) LIKE LOWER(:name)" +
                        "OR LOWER(m.playerTwo.name) LIKE LOWER(:name) " +
                        "OR LOWER(m.winner.name) LIKE LOWER(:name) " +
                        "ORDER BY m.id DESC",
                Match.class).setParameter("name", "%" + playerNameFilter + "%").
                            setFirstResult(offset).
                            setMaxResults(5).
                            getResultList();
        entityManager.close();
        System.out.println(matches);
        return matches;
    }

    @Override
    public int countMatchesWithPlayerName(String playerNameFilter) {
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

    @Override
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

    @Override
    public Match saveFinishedMatch(Match match) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(match);
            entityTransaction.commit();
            return match;
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new DatabaseException("Failed to save match " + match.getId(), e);
        } finally {
            entityManager.close();
        }
    }
}
