package com.tennis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "match")
public class Match {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "playerOne", referencedColumnName = "id")
    private Player playerOne;

    @ManyToOne
    @JoinColumn(name = "playerTwo", referencedColumnName = "id")
    private Player playerTwo;

    @ManyToOne
    @JoinColumn(name = "winner", referencedColumnName = "id")
    private Player winner;

    public Match(Player playerOne, Player playerTwo, Player winner) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.winner = winner;
    }

    public Match() {}

    public Integer getId() {
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
        return "\nid ='" + id + '\'' +
                ", playerOne ='" + playerOne.getId() + '\'' +
                ", playerTwo ='" + playerTwo.getId() + '\'' +
                ", winner ='" + winner.getId() + '\'' ;
    }
}

