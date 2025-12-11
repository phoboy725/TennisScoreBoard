package com.tennis.repositories;

import com.tennis.model.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PlayerDao {

    private final EntityManagerFactory entityManagerFactory;

    public PlayerDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Player> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Player> players = entityManager.createQuery("SELECT p FROM Player p", Player.class).getResultList();
        return players;
    }

    public Player getPlayerById(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Player> players = entityManager.createQuery("SELECT p FROM Player p WHERE p.id = :id", Player.class)
                .setParameter("id", id)
                .getResultList();
        return players.isEmpty() ? null : players.get(0);
    }

    public Player getPlayerByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Player> players = entityManager.createQuery("SELECT p FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", name)
                .getResultList();
        return players.isEmpty() ? null : players.get(0);
    }

    public void create(Player player) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(player);
            entityManager.getTransaction().commit();
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
