package com.tennis.dto;

import java.util.ArrayList;

public class MatchCurrentState {
    private Long playerOneId;
    private Long playerTwoId;
    private int playerOneSets = 0;
    private int playerOneGames = 0;
    private String playerOnePoints = "0";
    private ArrayList<Integer> playerOneSetsResult = new ArrayList<>();
    private int playerTwoSets = 0;
    private int playerTwoGames = 0;
    private String playerTwoPoints = "0";
    private ArrayList<Integer> playerTwoSetsResult = new ArrayList<>();
    private boolean matchFinished = false;
    private boolean tieBreak = false;
    private Long winnerId;

    public MatchCurrentState(Long playerOneId, Long playerTwoId) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
    }

    public Long getPlayerOneId() {
        return playerOneId;
    }

    public int getPlayerOneGames() {
        return playerOneGames;
    }

    public int getPlayerOneSets() {
        return playerOneSets;
    }

    public String getPlayerOnePoints() {
        return playerOnePoints;
    }

    public String getPlayerOneSetsResultSafe(int index) {
        if (playerOneSetsResult == null || index < 0 || index >= playerOneSetsResult.size()) {
            return "-";
        }
        return String.valueOf(playerOneSetsResult.get(index));
    }


    public Long getPlayerTwoId() {
        return playerTwoId;
    }

    public int getPlayerTwoGames() {
        return playerTwoGames;
    }

    public int getPlayerTwoSets() {
        return playerTwoSets;
    }

    public String getPlayerTwoPoints() {
        return playerTwoPoints;
    }

    public String getPlayerTwoSetsResultSafe(int index) {
        if (playerTwoSetsResult == null || index < 0 || index >= playerTwoSetsResult.size()) {
            return "-";
        }
        return String.valueOf(playerTwoSetsResult.get(index));
    }

    public boolean isMatchFinished() {
        return matchFinished;
    }

    public boolean isTieBreak() {
        return tieBreak;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setPlayerOneId(Long playerOneId) {
        this.playerOneId = playerOneId;
    }

    public void setPlayerOneGames(int playerOneGames) {
        this.playerOneGames = playerOneGames;
    }

    public void setPlayerOneSets(int playerOneSets) {
        this.playerOneSets = playerOneSets;
    }

    public void setPlayerOnePoints(String playerOnePoints) {
        this.playerOnePoints = playerOnePoints;
    }

    public void addPlayerOneSetsResult(int playerOneSetsResult) {
        this.playerOneSetsResult.add(playerOneSetsResult);
    }

    public void setPlayerTwoId(Long playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public void setPlayerTwoGames(int playerTwoGames) {
        this.playerTwoGames = playerTwoGames;
    }

    public void setPlayerTwoSets(int playerTwoSets) {
        this.playerTwoSets = playerTwoSets;
    }

    public void setPlayerTwoPoints(String playerTwoPoints) {
        this.playerTwoPoints = playerTwoPoints;
    }

    public void addPlayerTwoSetsResult(int playerTwoSetsResult) {
        this.playerTwoSetsResult.add(playerTwoSetsResult);
    }

    public void setMatchFinished(boolean matchFinished) {
        this.matchFinished = matchFinished;
    }

    public void setTieBreak(boolean isTieBreak) {
        this.tieBreak = isTieBreak;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }
}
