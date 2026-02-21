package com.tennis.domain;

public interface MatchState {

    void addPoint(OngoingMatch ongoingMatch, PlayerScored playerScored);
}
