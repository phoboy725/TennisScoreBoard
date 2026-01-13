package com.tennis.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "match_data")
public class Match {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_one_id", referencedColumnName = "id", nullable = false)
    private Player playerOne;

    @ManyToOne
    @JoinColumn(name = "player_two_id", referencedColumnName = "id", nullable = false)
    private Player playerTwo;

    @ManyToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id", nullable = false)
    private Player winner;

    public Match(Player playerOne, Player playerTwo, Player winner) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.winner = winner;
    }

    public Match() {}

    public Long getId() {
        return id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getWinner() {
        return winner;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", playerOneId=" + playerOne.getId() +
                ", playerTwoId=" + playerTwo.getId() +
                ", winnerId=" + winner.getId() +
                "}";
    }
}

