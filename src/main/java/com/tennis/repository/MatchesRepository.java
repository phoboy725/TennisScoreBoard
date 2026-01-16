package com.tennis.repository;

import com.tennis.entity.Match;

import java.util.List;

public interface MatchesRepository {
    List<Match> findAll(int offset, int limit);

    List<Match> findMatchesByPlayerName(String playerNameFilter, int offset, int limit);

    Long countMatchesWithPlayerName(String playerNameFilter);

    Long countAll();

    Match save(Match match);
}
