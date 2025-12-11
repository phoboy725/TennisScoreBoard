package com.tennis.service;

import com.tennis.dto.MatchCurrentState;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.model.Match;
import com.tennis.model.Player;
import com.tennis.repositories.MatchesDao;
import com.tennis.repositories.PlayerDao;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {

    private final MatchesDao matchesDao;
    private final PlayerDao playerDao;

    private final Map<UUID, MatchCurrentState> currentMatches = new ConcurrentHashMap<>();

    public MatchService(MatchesDao matchesDao, PlayerDao playerDao) {
        this.matchesDao = matchesDao;
        this.playerDao = playerDao;
    }

    public MatchCurrentState getMatch(UUID matchId) {
        MatchCurrentState match = currentMatches.get(matchId);
        if (match == null) {
            throw new MatchNotFoundException("Match not found " + matchId);
        }
        return match;
    }

    public UUID createMatch(Integer playerOneId, Integer playerTwoId) {
        UUID matchId = UUID.randomUUID();
        currentMatches.put(matchId, new MatchCurrentState(playerOneId, playerTwoId));
        return matchId;
    }

    public void updateScore(UUID matchId, Integer scoreButtonId) {
        MatchCurrentState match = currentMatches.get(matchId);
        if (match == null) {
            throw new MatchNotFoundException("Match not found: " + matchId);
        }

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

        MatchCurrentState currentState = currentMatches.get(matchId);
        if (currentState == null) {
            throw new MatchNotFoundException("Match not found: " + matchId);
        }

        if (!currentState.isMatchFinished() || currentState.getWinnerId() == null) {
            return;
        }

        Player playerOne = playerDao.getPlayerById(currentState.getPlayerOneId());
        Player playerTwo = playerDao.getPlayerById(currentState.getPlayerTwoId());
        Player winner = playerDao.getPlayerById(currentState.getWinnerId());

        Match match = new Match();
        match.setPlayerOne(playerOne);
        match.setPlayerTwo(playerTwo);
        match.setWinner(winner);
        matchesDao.saveFinishedMatch(match);
        currentMatches.remove(matchId);
    }

    public List<Match> getMatches(String filterByPlayerName, int offset) {
        if (filterByPlayerName != null && !filterByPlayerName.trim().isEmpty()) {
            return matchesDao.findByPlayerName(filterByPlayerName, offset);
        }
        return matchesDao.readAll(offset);
    }

    public int getTotalMatchesCount(String filterByPlayerName) {
        if (filterByPlayerName != null && !filterByPlayerName.trim().isEmpty()) {
            return matchesDao.countByPlayerName(filterByPlayerName);
        }
        return matchesDao.countAll();
    }
}

