package com.tennis.service;

import com.tennis.entity.Player;
import com.tennis.exception.DatabaseException;
import com.tennis.repository.PlayerRepository;
import com.tennis.util.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Optional;

public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getOrCreatePlayer(String playerName) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Optional<Player> foundPlayer = playerRepository.findByName(playerName);
        if (foundPlayer.isPresent()) {
            return foundPlayer.get();
        }
        try {
            transaction.begin();
            Player player = new Player(playerName);
            entityManager.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to create player " + playerName, e);
        }
    }

    public Optional<Player> findPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Optional<Player> findPlayerByName(String name) {
        return playerRepository.findByName(name);
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
