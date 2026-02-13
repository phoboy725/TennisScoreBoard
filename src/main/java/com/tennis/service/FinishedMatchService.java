package com.tennis.service;

import com.tennis.entity.Match;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class FinishedMatchService {

    private final MatchesRepository matchesRepository;
    private final PlayerRepository playerRepository;

    public FinishedMatchService(MatchesRepository matchesRepository, PlayerRepository playerRepository) {
        this.matchesRepository = matchesRepository;
        this.playerRepository = playerRepository;
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

}


