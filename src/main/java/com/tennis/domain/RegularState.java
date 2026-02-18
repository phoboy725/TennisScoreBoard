package com.tennis.domain;

final class RegularState implements MatchState {

    private static final int VICTORY_SET_GAMES = 6;
    private static final int MIN_ADVANTAGE_TO_WIN_SET = 2;
    private static final int VICTORY_MATCH_SETS = 2;

    @Override
    public void addPoint(OngoingMatch ongoingMatch, PlayerScored playerScored) {

        PlayerScore winner = ongoingMatch.winnerOfPoint(playerScored);
        PlayerScore loser  = ongoingMatch.loserOfPoint(playerScored);

        applyRegularPoint(ongoingMatch, winner, loser);
    }

    private void applyRegularPoint(OngoingMatch ongoingMatch, PlayerScore winner, PlayerScore loser) {

        if (winner.isForty() && loser.isForty()) {
            winner.setPoints(TennisPoint.ADVANTAGE);
            return;
        }

        if (winner.hasAdvantage()) {
            winGame(ongoingMatch, winner, loser);
            return;
        }

        if (loser.hasAdvantage()) {
            winner.setPoints(TennisPoint.FORTY);
            loser.setPoints(TennisPoint.FORTY);
            return;
        }

        if (winner.isForty() && !loser.isForty()) {
            winGame(ongoingMatch, winner, loser);
            return;
        }

        winner.incrementRegularPoint();
    }

    private void winGame(OngoingMatch ongoingMatch, PlayerScore winner, PlayerScore loser) {
        winner.incrementGames();
        winner.resetPoints();
        loser.resetPoints();

        if (winner.getGames() == 6 && loser.getGames() == 6) {
            winner.resetTieBreakPoints();
            loser.resetTieBreakPoints();
            ongoingMatch.setState(new TieBreakState());
            return;
        }

        if (winner.getGames() >= VICTORY_SET_GAMES
                && (winner.getGames() - loser.getGames()) >= MIN_ADVANTAGE_TO_WIN_SET) {
            winSet(ongoingMatch, winner, loser);
        }
    }

    private void winSet(OngoingMatch ongoingMatch, PlayerScore winner, PlayerScore loser) {
        winner.incrementSets();

        winner.addSetResult(winner.getGames());
        loser.addSetResult(loser.getGames());

        winner.resetGames();
        loser.resetGames();
        winner.resetPoints();
        loser.resetPoints();

        if (winner.getSets() >= VICTORY_MATCH_SETS) {
            ongoingMatch.finishWithWinner(winner);
        }
    }
}
