package com.tennis.persistence;

import com.tennis.exception.DatabaseException;
import com.tennis.entity.Match;
import com.tennis.repository.MatchesRepository;
import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class JpaMatchesRepository implements MatchesRepository {

    private static final String SELECT_ALL_JPQL = """
            SELECT m 
            FROM Match m
            JOIN FETCH m.playerOne 
            JOIN FETCH m.playerTwo 
            JOIN FETCH m.winner 
            """;

    private static final String COUNT_ALL_JPQL = """
            SELECT COUNT (m) 
            FROM Match m
            """;

    private static final String FILTER_BY_NAME_JPQL = """
            WHERE LOWER(m.playerOne.name) LIKE LOWER(:name)
            OR LOWER(m.playerTwo.name) LIKE LOWER(:name) 
            """;

    private static final String ORDER_BY_ID_DESC = """
            ORDER BY m.id DESC
            """;

    private static final String FIND_BY_NAME_JPQL =
            SELECT_ALL_JPQL + FILTER_BY_NAME_JPQL;

    private static final String COUNT_WITH_NAME_JPQL =
            COUNT_ALL_JPQL + FILTER_BY_NAME_JPQL;

    @Override
    public List<Match> findAll(int offset, int limit) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<Match> matches = entityManager.createQuery(SELECT_ALL_JPQL + ORDER_BY_ID_DESC, Match.class).
                    setFirstResult(offset).
                    setMaxResults(limit).
                    getResultList();
            transaction.commit();
            return matches;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to find matches", e);
        }
    }

    @Override
    public List<Match> findMatchesByPlayerName(String playerNameFilter, int offset, int limit) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<Match> matches = entityManager.createQuery(FIND_BY_NAME_JPQL + ORDER_BY_ID_DESC, Match.class).
                    setParameter("name", "%" + playerNameFilter + "%").
                    setFirstResult(offset).
                    setMaxResults(limit).
                    getResultList();
            transaction.commit();
            return matches;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to find matches", e);
        }
    }

    @Override
    public Long countMatchesWithPlayerName(String playerNameFilter) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Long count = entityManager.createQuery(COUNT_WITH_NAME_JPQL, Long.class)
                    .setParameter("name", "%" + playerNameFilter + "%")
                    .getSingleResult()
                    .longValue();
            transaction.commit();
            return count;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to count matches", e);
        }
    }

    @Override
    public Long countAll() {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Long count = entityManager.createQuery(COUNT_ALL_JPQL, Long.class).
                    getSingleResult();
            transaction.commit();
            return count;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to count matches", e);
        }
    }

    @Override
    public Match save(Match match) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(match);
            transaction.commit();
            return match;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to save match", e);
        }
    }

    private void safeRollback(EntityTransaction transaction, Exception originalException) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }
}
