package com.tennis.service;

import com.tennis.dto.PlayerRequestDto;
import com.tennis.entity.Player;
import com.tennis.exception.DatabaseException;
import com.tennis.mapper.CreatePlayerMapper;
import com.tennis.repository.PlayerRepository;
import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(String playerName) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
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
