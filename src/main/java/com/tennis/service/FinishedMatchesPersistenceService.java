package com.tennis.service;

import com.tennis.model.Match;
import com.tennis.repositories.MatchesDao;

import java.util.List;

public class FinishedMatchesPersistenceService {

    private static MatchesDao matchesDAO;

    public static List<Match> getAllMatchesFromDB() {
        return matchesDAO.readAll();
    }
}
