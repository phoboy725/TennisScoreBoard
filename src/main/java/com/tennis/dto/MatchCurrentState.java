package com.tennis.dto;

import java.util.ArrayList;

public class MatchCurrentState {
    private int playerOneId;
    private int playerTwoId;
    private int playerOneSets = 0;
    private int playerOneGames = 0;
    private String playerOnePoints = "0";
    private ArrayList<Integer> playerOneSetsResult = new ArrayList<>();
    private int playerTwoSets = 0;
    private int playerTwoGames = 0;
    private String playerTwoPoints = "0";
    private ArrayList<Integer> playerTwoSetsResult = new ArrayList<>();
    private boolean isMatchFinished = false;
    private boolean isTieBreak = false;
    private Integer winnerId;

    public MatchCurrentState(int playerOneId, int playerTwoId) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
    }

    public int getPlayerOneId() {
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


    public int getPlayerTwoId() {
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
        return isMatchFinished;
    }

    public boolean isTieBreak() {
        return isTieBreak;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public void setPlayerOneId(int playerOneId) {
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

    public void setPlayerOneSetsResult(int playerOneSetsResult) {
        this.playerOneSetsResult.add(playerOneSetsResult);
    }

    public void setPlayerTwoId(int playerTwoId) {
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

    public void setPlayerTwoSetsResult(int playerTwoSetsResult) {
        this.playerTwoSetsResult.add(playerTwoSetsResult);
    }

    public void setMatchFinished(boolean isMatchFinished) {
        this.isMatchFinished = isMatchFinished;
    }

    public void setTieBreak(boolean isTieBreak) {
        this.isTieBreak = isTieBreak;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}
