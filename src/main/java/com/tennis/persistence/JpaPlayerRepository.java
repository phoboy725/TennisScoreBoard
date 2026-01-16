package com.tennis.persistence;

import com.tennis.entity.Player;
import com.tennis.exception.DatabaseException;
import com.tennis.repository.PlayerRepository;
import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class JpaPlayerRepository implements PlayerRepository {

    private static final String SELECT_ALL_JPQL =
        """
        SELECT p
        FROM Player p
        """;

    private static final String FILTER_BY_NAME_JPQL = """
        WHERE p.name = :name
        """;

    private static final String FIND_BY_NAME_JPQL =
            SELECT_ALL_JPQL + FILTER_BY_NAME_JPQL;

    @Override
    public Player findPlayerById(Long id) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        return entityManager.find(Player.class, id);
    }

    @Override
    public Player findPlayerByName(String name) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<Player> players = entityManager.createQuery(FIND_BY_NAME_JPQL, Player.class)
                    .setParameter("name", name)
                    .getResultList();
            transaction.commit();
            return players.isEmpty() ? null : players.get(0);

        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to find player", e);
        }
    }

    @Override
    public Player save(Player player) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(player);
            transaction.commit();

            return player;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to save player", e);
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
