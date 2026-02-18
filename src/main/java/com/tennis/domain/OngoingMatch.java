
package com.tennis.domain;

import java.util.Objects;

public final class OngoingMatch {

    private final PlayerScore playerOneScore;
    private final PlayerScore playerTwoScore;

    private MatchState state;
    private Long winnerId;

    public OngoingMatch(Long playerOneId, Long playerTwoId) {
        this.playerOneScore = new PlayerScore(playerOneId);
        this.playerTwoScore = new PlayerScore(playerTwoId);
        this.state = new RegularState();
    }

    public void pointWonBy(PlayerScored playerScored) {
        Objects.requireNonNull(playerScored, "playerScored");
        state.addPoint(this, playerScored);
    }

    PlayerScore winnerOfPoint(PlayerScored who) {
        return who == PlayerScored.ONE ? playerOneScore : playerTwoScore;
    }

    PlayerScore loserOfPoint(PlayerScored who) {
        return who == PlayerScored.ONE ? playerTwoScore : playerOneScore;
    }

    void setState(MatchState newState) {
        this.state = Objects.requireNonNull(newState, "newState");
    }

    void finishWithWinner(PlayerScore winner) {
        this.winnerId = winner.getId();
        this.state = new FinishedState();
    }

    public boolean isFinished() {
        return winnerId != null;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public PlayerScore getPlayerOneScore() { return playerOneScore; }
    public PlayerScore getPlayerTwoScore() { return playerTwoScore; }

    public MatchState getState() { return this.state;}
}
