package com.tennis.service;

import com.tennis.dto.MatchCurrentState;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {

    private static final MatchService INSTANCE = new MatchService();
    private Map<UUID, MatchCurrentState> currentMatches = new ConcurrentHashMap<>();

    private MatchService() {}

    public static MatchService getInstance() {
        return INSTANCE;
    }

    public MatchCurrentState getMatch(UUID matchId) {
        return currentMatches.get(matchId);
    }

    public UUID createMatch(Integer playerOneId, Integer playerTwoId) {
        UUID matchId = UUID.randomUUID();
        currentMatches.put(matchId, new MatchCurrentState(playerOneId, playerTwoId));
        return matchId;
    }

    public void updateScore(UUID matchId, Integer scoreButtonId) {
        MatchCurrentState match = currentMatches.get(matchId);
        MatchScoreService score = MatchScoreService.getInstance();
        if (!match.isMatchFinished()) {
            if (match.isTieBreak()) {
                score.countTieBreak(match, scoreButtonId);
            } else {
                score.countPoints(match, scoreButtonId);
            }
        }
    }

    public void finishMatch(UUID matchId) {

        currentMatches.remove(matchId);
    }

}

