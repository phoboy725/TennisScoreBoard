package com.tennis.domain;

final class TieBreakState implements MatchState {

    private static final int VICTORY_TIEBREAK_POINTS = 7;
    private static final int MIN_ADVANTAGE_TO_WIN_TB = 2;
    private static final int VICTORY_MATCH_SETS = 2;

    @Override
    public void addPoint(OngoingMatch ongoingMatch, PlayerScored playerScored) {
        PlayerScore winner = ongoingMatch.winnerOfPoint(playerScored);
        PlayerScore loser  = ongoingMatch.loserOfPoint(playerScored);

        winner.incrementTieBreakPoints();

        int wp = winner.getTieBreakPoints();
        int lp = loser.getTieBreakPoints();

        if (wp >= VICTORY_TIEBREAK_POINTS && (wp - lp) >= MIN_ADVANTAGE_TO_WIN_TB) {

            winner.incrementSets();

            winner.addSetResult(7);
            loser.addSetResult(6);

            winner.resetTieBreakPoints();
            loser.resetTieBreakPoints();

            winner.resetGames();
            loser.resetGames();

            winner.resetPoints();
            loser.resetPoints();

            if (winner.getSets() >= VICTORY_MATCH_SETS) {
                ongoingMatch.finishWithWinner(winner);
                return;
            }

            ongoingMatch.setState(new RegularState());
        }
    }
}