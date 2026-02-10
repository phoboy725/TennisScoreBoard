package com.tennis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlayerScore {

    private final Long id;
    private TennisPoint points = TennisPoint.LOVE;
    private int games = 0;
    private int sets = 0;
    private int tieBreakPoints = 0;

    private final List<Integer> setsResult = new ArrayList<>();

    public PlayerScore(Long playerId) {
        this.id = playerId;
    }

    public Long getId() {
        return id;
    }

    public TennisPoint getPoints() {
        return points;
    }

    public String getPointsDisplay() {
        return points.getDisplayValue();
    }

    public int getGames() {
        return games;
    }

    public int getSets() {
        return sets;
    }

    public int getTieBreakPoints() {
        return tieBreakPoints;
    }

    // чтобы JSP мог читать, но не менять список
    public List<Integer> getSetsResult() {
        return Collections.unmodifiableList(setsResult);
    }

    public String getSetsResultSafe(int index) {
        if (index < 0 || index >= setsResult.size()) return "-";
        return String.valueOf(setsResult.get(index));
    }

    public boolean hasAdvantage() {
        return points == TennisPoint.ADVANTAGE;
    }

    public boolean isForty() {
        return points == TennisPoint.FORTY;
    }

    // ===== package-private mutators for states =====

    void setPoints(TennisPoint newPoints) {
        this.points = newPoints;
    }

    void incrementRegularPoint() {
        this.points = this.points.next();
    }

    void resetPoints() {
        this.points = TennisPoint.LOVE;
    }

    void incrementGames() {
        this.games++;
    }

    void resetGames() {
        this.games = 0;
    }

    void incrementSets() {
        this.sets++;
    }

    void incrementTieBreakPoints() {
        this.tieBreakPoints++;
    }

    void resetTieBreakPoints() {
        this.tieBreakPoints = 0;
    }

    void addSetResult(int gamesWonInSet) {
        this.setsResult.add(gamesWonInSet);
    }
}
