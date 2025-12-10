package com.tennis.dto;

import java.util.ArrayList;

public class MatchCurrentState {
    private Integer playerOneId;
    private Integer playerTwoId;
    private Integer playerOneSets = 0;
    private Integer playerOneGames = 0;
    private String playerOnePoints = "0";
    private ArrayList<Integer> playerOneSetsResult = new ArrayList<>();
    private Integer playerTwoSets = 0;
    private Integer playerTwoGames = 0;
    private String playerTwoPoints = "0";
    private ArrayList<Integer> playerTwoSetsResult = new ArrayList<>();
    private boolean isMatchFinished = false;
    private boolean isDeuce = false;
    private boolean isTieBreak = false;
    private Integer winnerId;

    public MatchCurrentState(Integer playerOneId, Integer playerTwoId) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
    }

    public Integer getPlayerOneId() {
        return playerOneId;
    }

    public Integer getPlayerOneGames() {
        return playerOneGames;
    }

    public Integer getPlayerOneSets() {
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


    public Integer getPlayerTwoId() {
        return playerTwoId;
    }

    public Integer getPlayerTwoGames() {
        return playerTwoGames;
    }

    public Integer getPlayerTwoSets() {
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

    public boolean isDeuce() {
        return isDeuce;
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

    public void setPlayerOneId(Integer playerOneId) {
        this.playerOneId = playerOneId;
    }

    public void setPlayerOneGames(Integer playerOneGames) {
        this.playerOneGames = playerOneGames;
    }

    public void setPlayerOneSets(Integer playerOneSets) {
        this.playerOneSets = playerOneSets;
    }

    public void setPlayerOnePoints(String playerOnePoints) {
        this.playerOnePoints = playerOnePoints;
    }

    public void setPlayerOneSetsResult(Integer playerOneSetsResult) {
        this.playerOneSetsResult.add(playerOneSetsResult);
    }

    public void setPlayerTwoId(Integer playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public void setPlayerTwoGames(Integer playerTwoGames) {
        this.playerTwoGames = playerTwoGames;
    }

    public void setPlayerTwoSets(Integer playerTwoSets) {
        this.playerTwoSets = playerTwoSets;
    }

    public void setPlayerTwoPoints(String playerTwoPoints) {
        this.playerTwoPoints = playerTwoPoints;
    }

    public void setPlayerTwoSetsResult(Integer playerTwoSetsResult) {
        this.playerTwoSetsResult.add(playerTwoSetsResult);
    }

    public void setMatchFinished(boolean isMatchFinished) {
        this.isMatchFinished = isMatchFinished;
    }

    public void setDeuce(boolean isDeuce) {
        this.isDeuce = isDeuce;
    }

    public void setTieBreak(boolean isTieBreak) {
        this.isTieBreak = isTieBreak;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}
