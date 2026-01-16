package com.tennis.persistence;

import com.tennis.entity.Player;
import com.tennis.repository.PlayerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class JpaPlayerRepository implements PlayerRepository {

    private static final String SELECT_ALL_JPQL =
        """
        SELECT p
        FROM Player p
        """;

    private static final String FILTER_BY_ID_JPQL = """
        WHERE p.id = :id
        """;

    private static final String FILTER_BY_NAME_JPQL = """
        WHERE p.name = :name
        """;

    private static final String FIND_BY_ID_JPQL =
            SELECT_ALL_JPQL + FILTER_BY_ID_JPQL;

    private static final String FIND_BY_NAME_JPQL =
            SELECT_ALL_JPQL +
                    FILTER_BY_NAME_JPQL;

    private final EntityManagerFactory entityManagerFactory;

    public JpaPlayerRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Player findPlayerById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Player> players = entityManager.createQuery(FIND_BY_ID_JPQL, Player.class)
                .setParameter("id", id)
                .getResultList();
        return players.isEmpty() ? null : players.get(0);
    }

    @Override
    public Player findPlayerByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Player> players = entityManager.createQuery(FIND_BY_NAME_JPQL, Player.class)
                .setParameter("name", name)
                .getResultList();
        return players.isEmpty() ? null : players.get(0);
    }

    @Override
    public Player save(Player player) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(player);
            entityManager.getTransaction().commit();
            return player;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }
}
