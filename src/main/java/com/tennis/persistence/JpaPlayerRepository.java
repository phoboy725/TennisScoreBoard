package com.tennis.persistence;

import com.tennis.entity.Player;
import com.tennis.exception.DatabaseException;
import com.tennis.repository.PlayerRepository;
import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

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
    public Optional<Player> findPlayerByName(String name) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        try {
            Player player = entityManager.createQuery(FIND_BY_NAME_JPQL, Player.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(player);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Player save(Player player) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        try {
            entityManager.persist(player);
            return player;
        } catch (Exception e) {
            throw new DatabaseException("Failed to save player", e);
        }
    }
}
