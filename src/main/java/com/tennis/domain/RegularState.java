package com.tennis.domain;

final class RegularState implements MatchState {

    private static final int VICTORY_SET_GAMES = 6;
    private static final int MIN_ADVANTAGE_TO_WIN_SET = 2;
    private static final int VICTORY_MATCH_SETS = 2;

    @Override
    public void addPoint(OngoingMatch match, PlayerScored playerScored) {
        PlayerScore winner = match.winnerOfPoint(playerScored);
        PlayerScore loser  = match.loserOfPoint(playerScored);

        applyRegularPoint(match, winner, loser);
    }

    private void applyRegularPoint(OngoingMatch match, PlayerScore winner, PlayerScore loser) {

        if (winner.isForty() && loser.isForty()) {
            winner.setPoints(TennisPoint.ADVANTAGE);
            return;
        }

        if (winner.hasAdvantage()) {
            winGame(match, winner, loser);
            return;
        }

        if (loser.hasAdvantage()) {
            winner.setPoints(TennisPoint.FORTY);
            loser.setPoints(TennisPoint.FORTY);
            return;
        }

        if (winner.isForty() && !loser.isForty()) {
            winGame(match, winner, loser);
            return;
        }

        winner.incrementRegularPoint();
    }

    private void winGame(OngoingMatch match, PlayerScore winner, PlayerScore loser) {
        winner.incrementGames();
        winner.resetPoints();
        loser.resetPoints();

        if (winner.getGames() == 6 && loser.getGames() == 6) {
            winner.resetTieBreakPoints();
            loser.resetTieBreakPoints();
            match.setState(new TieBreakState());
            return;
        }

        if (winner.getGames() >= VICTORY_SET_GAMES
                && (winner.getGames() - loser.getGames()) >= MIN_ADVANTAGE_TO_WIN_SET) {
            winSet(match, winner, loser);
        }
    }

    private void winSet(OngoingMatch match, PlayerScore winner, PlayerScore loser) {
        winner.incrementSets();

        // ✅ сохраняем результат сета (например 6:3 или 7:5)
        winner.addSetResult(winner.getGames());
        loser.addSetResult(loser.getGames());

        // reset for next set
        winner.resetGames();
        loser.resetGames();
        winner.resetPoints();
        loser.resetPoints();

        if (winner.getSets() >= VICTORY_MATCH_SETS) {
            match.finishWithWinner(winner);
        }
    }
}
