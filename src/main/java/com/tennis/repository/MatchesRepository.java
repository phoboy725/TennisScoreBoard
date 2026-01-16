package com.tennis.repository;

import com.tennis.entity.Match;

import java.util.List;

public interface MatchesRepository {
    List<Match> readAll(int offset);

    List<Match> findMatchesByPlayerName(String playerNameFilter, int offset);

    int countMatchesWithPlayerName(String playerNameFilter);

    int countAll();

    Match saveFinishedMatch(Match match);
}
