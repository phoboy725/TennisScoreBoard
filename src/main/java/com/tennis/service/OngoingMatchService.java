package com.tennis.service;

import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScored;
import com.tennis.dto.MatchUpdateDto;
import com.tennis.exception.DatabaseException;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.entity.Match;
import com.tennis.entity.Player;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;
import com.tennis.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchService {

    private final MatchesRepository matchesRepository;
    private final PlayerRepository playerRepository;

    private final Map<UUID, OngoingMatch> matches = new ConcurrentHashMap<>();

    public OngoingMatchService(MatchesRepository matchesRepository, PlayerRepository playerRepository) {
        this.matchesRepository = matchesRepository;
        this.playerRepository = playerRepository;
    }

    public Optional<OngoingMatch> findMatch(UUID matchId) {
        return Optional.ofNullable(matches.get(matchId));
    }

    public UUID createMatch(Long playerOneId, Long playerTwoId) {
        UUID uuid = UUID.randomUUID();
        matches.put(uuid, new OngoingMatch(playerOneId, playerTwoId));
        return uuid;
    }

    public MatchUpdateDto updateScoreAndFinishMatchIfNeeded(UUID matchId, PlayerScored playerScored) {

        class Holder {
            OngoingMatch match;
            boolean finished;
        }

        Holder holder = new Holder();

        matches.compute(matchId, (k, v) -> {
            if (v == null) {
                throw new MatchNotFoundException("Match not found: " + matchId);
            }

            v.pointWonBy(playerScored);

            holder.match = v;
            holder.finished = v.isFinished() && v.getWinnerId() != null;

            return holder.finished ? null : v;
        });

        if (holder.finished) {
            persistFinishedMatch(holder.match);
        }

        return new MatchUpdateDto(holder.match, holder.finished);
    }

    public Match persistFinishedMatch(OngoingMatch ongoingMatch) {
        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Match match;
        try {
            transaction.begin();
            Player playerOne = entityManager.getReference(Player.class, ongoingMatch.getPlayerOneScore().getId());
            Player playerTwo = entityManager.getReference(Player.class, ongoingMatch.getPlayerTwoScore().getId());
            Player winner = entityManager.getReference(Player.class, ongoingMatch.getWinnerId());
            match = new Match(playerOne, playerTwo, winner);
            matchesRepository.save(match);
            transaction.commit();
        } catch (Exception e) {
            safeRollback(transaction, e);
            throw new DatabaseException("Failed to finish match ", e);
        }
        return match;
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

