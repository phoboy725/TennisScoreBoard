package com.tennis.dto;

public class MatchCurrentState {
    private Integer playerOneId;
    private Integer playerTwoId;
    private Integer playerOneSets = 0;
    private Integer playerOneGames = 0;
    private String playerOnePoints = "0";
    private Integer playerTwoSets = 0;
    private Integer playerTwoGames = 0;
    private String playerTwoPoints = "0";
    private boolean isMatchFinished = false;
    private boolean isDeuce = false;
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

    public boolean isDeuce() {
        return isDeuce;
    }

    public boolean isMatchFinished() {
        return isMatchFinished;
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

    public void setMatchFinished(boolean isMatchFinished) {
        this.isMatchFinished = isMatchFinished;
    }

    public void setDeuce(boolean isDeuce) {
        this.isDeuce = isDeuce;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }
}
