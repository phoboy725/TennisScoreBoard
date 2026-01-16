package com.tennis.service;

import com.tennis.dto.MatchCurrentState;
import com.tennis.exception.MatchNotFoundException;
import com.tennis.entity.Match;
import com.tennis.entity.Player;
import com.tennis.repository.MatchesRepository;
import com.tennis.repository.PlayerRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {

    private final MatchesRepository matchesDao;
    private final PlayerRepository playerDao;

    private final Map<UUID, MatchCurrentState> currentMatches = new ConcurrentHashMap<>();

    public MatchService(MatchesRepository matchesDao, PlayerRepository playerDao) {
        this.matchesDao = matchesDao;
        this.playerDao = playerDao;
    }

    public MatchCurrentState findMatch(UUID matchId) {
        return currentMatches.get(matchId);
    }

    public UUID createMatch(Long playerOneId, Long playerTwoId) {
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
            return;
        }

        if (!currentState.isMatchFinished() || currentState.getWinnerId() == null) {
            return;
        }

        Player playerOne = playerDao.findPlayerById(currentState.getPlayerOneId());
        Player playerTwo = playerDao.findPlayerById(currentState.getPlayerTwoId());
        Player winner = playerDao.findPlayerById(currentState.getWinnerId());

        Match match = new Match();
        match.setPlayerOne(playerOne);
        match.setPlayerTwo(playerTwo);
        match.setWinner(winner);
        matchesDao.saveFinishedMatch(match);
        currentMatches.remove(matchId);
    }

    public List<Match> getMatches(String filterByPlayerName, int offset) {
        if (filterByPlayerName != null && !filterByPlayerName.trim().isEmpty()) {
            return matchesDao.findMatchesByPlayerName(filterByPlayerName, offset);
        }
        return matchesDao.readAll(offset);
    }

    public int getTotalMatchesCount(String filterByPlayerName) {
        if (filterByPlayerName != null && !filterByPlayerName.trim().isEmpty()) {
            return matchesDao.countMatchesWithPlayerName(filterByPlayerName);
        }
        return matchesDao.countAll();
    }
}

