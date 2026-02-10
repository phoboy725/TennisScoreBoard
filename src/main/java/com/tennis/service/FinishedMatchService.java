package com.tennis.service;

import com.tennis.dto.MatchCurrentState;
import com.tennis.exception.DatabaseException;
import com.tennis.exception.MatchNotFinishedException;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.entity.Match;
import com.tennis.entity.Player;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;
import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FinishedMatchService {

    private final MatchesRepository matchesRepository;
    private final PlayerRepository playerRepository;
    private final MatchScoreService score;

    private final Map<UUID, MatchCurrentState> currentMatches = new ConcurrentHashMap<>();

    public FinishedMatchService(MatchesRepository matchesRepository, PlayerRepository playerRepository, MatchScoreService score) {
        this.matchesRepository = matchesRepository;
        this.playerRepository = playerRepository;
        this.score = score;
    }

    public Match finishMatch(UUID matchId) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        MatchCurrentState currentState = checkFinishedState(matchId);
        Match match;
        try {
            transaction.begin();
            Player playerOne = entityManager.getReference(Player.class, currentState.getPlayerOneId());
            Player playerTwo = entityManager.getReference(Player.class, currentState.getPlayerTwoId());
            Player winner = entityManager.getReference(Player.class, currentState.getWinnerId());
            match = new Match(playerOne, playerTwo, winner);
            matchesRepository.save(match);
            transaction.commit();
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to finish match " + matchId, e);
        }
        currentMatches.remove(matchId);
        return match;
    }

    private MatchCurrentState checkFinishedState(UUID matchId) {
        MatchCurrentState state = currentMatches.get(matchId);
        if (state == null) {
            throw new MatchNotFoundException("No match with this ID");
        }
        if (!state.isMatchFinished() || state.getWinnerId() == null) {
            throw new MatchNotFinishedException("Match is not finished");
        }
        return state;
    }

    public List<Match> getMatches(String filterByPlayerName, int offset, int limit) {
        if (isPlayerFilterApplied(filterByPlayerName)) {
            return matchesRepository.findMatchesByPlayerName(filterByPlayerName, offset, limit);
        }
        return matchesRepository.findAll(offset, limit);
    }

    public Long getTotalMatchesCount(String filterByPlayerName) {
        if (isPlayerFilterApplied(filterByPlayerName)) {
            return matchesRepository.countMatchesWithPlayerName(filterByPlayerName);
        }
        return matchesRepository.countAll();
    }

    private boolean isPlayerFilterApplied(String filterByPlayerName) {
        return filterByPlayerName != null && !filterByPlayerName.isBlank();
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


