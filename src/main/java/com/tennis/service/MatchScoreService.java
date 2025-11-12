package com.tennis.service;

import com.tennis.dto.MatchCurrentState;
import com.tennis.enums.TennisPoints;

public class MatchScoreService {

    private static final MatchScoreService INSTANCE = new MatchScoreService();

    public static MatchScoreService getInstance() {
        return INSTANCE;
    }

    private String convertPointsToTennis(Integer from) {
        var to = switch (from) {
            case 0 -> "0";
            case 1 -> TennisPoints.FIFTEEN.getDisplay();
            case 2 -> "30";
            case 3 -> "40";
            case 4 -> "AD";
            case 5 -> "00";
            default -> throw new IllegalStateException("Unexpected value: " + from);
        };
        return to;
    }

    private Integer convertPointsFromTennis(String from) {
        var to = switch (from) {
            case "0" -> 0;
            case "15" -> 1;
            case "30" -> 2;
            case "40" -> 3;
            case "AD" -> 4;
            case "00" -> 5;
            default -> throw new IllegalStateException("Unexpected value: " + from);
        };
        return to;
    }

    public void countPoints(MatchCurrentState match, Integer scoreButtonId) {
        Integer playerOnePoints = convertPointsFromTennis(match.getPlayerOnePoints());
        Integer playerTwoPoints = convertPointsFromTennis(match.getPlayerTwoPoints());
        if (playerOnePoints == 3 && playerTwoPoints == 3) {
            if (scoreButtonId == 1) {
                match.setPlayerOnePoints("AD");
            }
            if (scoreButtonId == 2) {
                match.setPlayerTwoPoints("AD");
            }
        } else if (playerOnePoints == 3 && playerTwoPoints == 4) {
            if (scoreButtonId == 1) {
                match.setPlayerTwoPoints("40");
            }
            if (scoreButtonId == 2) {
                match.setPlayerTwoGames(match.getPlayerTwoGames() + 1);
                countGames(match);
                match.setPlayerOnePoints("0");
                match.setPlayerTwoPoints("0");
                return;
            }
        } else if (playerOnePoints == 4 && playerTwoPoints == 3) {
            if (scoreButtonId == 2) {
                match.setPlayerOnePoints("40");
            }
            if (scoreButtonId == 1) {
                match.setPlayerOneGames(match.getPlayerOneGames() + 1);
                countGames(match);
                match.setPlayerOnePoints("0");
                match.setPlayerTwoPoints("0");
                return;
            }
        } else if (playerOnePoints == 3 && scoreButtonId == 1) {
            match.setPlayerOneGames(match.getPlayerOneGames() + 1);
            countGames(match);
            match.setPlayerOnePoints("0");
            match.setPlayerTwoPoints("0");
            return;
        } else if (playerTwoPoints == 3 && scoreButtonId == 2) {
            match.setPlayerTwoGames(match.getPlayerTwoGames() + 1);
            countGames(match);
            match.setPlayerOnePoints("0");
            match.setPlayerTwoPoints("0");
            return;
    } else {
            if (scoreButtonId == 1) {
                match.setPlayerOnePoints(convertPointsToTennis(playerOnePoints + 1));
            }
            if (scoreButtonId == 2) {
                match.setPlayerTwoPoints(convertPointsToTennis(playerTwoPoints + 1));
            }
        }
    }

    public void countTieBreak(MatchCurrentState match, Integer scoreButtonId) {
        Integer playerOnePoints = Integer.parseInt(match.getPlayerOnePoints());
        Integer playerTwoPoints = Integer.parseInt(match.getPlayerTwoPoints());
        if ((playerOnePoints >= 7 || playerTwoPoints >= 7) && ((playerOnePoints - playerTwoPoints < Math.abs(2)))) {
            if (playerOnePoints > playerTwoPoints) {
                match.setPlayerOneSetsResult(match.getPlayerOneGames() + 1);
                match.setPlayerTwoSetsResult(match.getPlayerTwoGames());
            } else {
                match.setPlayerOneSetsResult(match.getPlayerOneGames());
                match.setPlayerTwoSetsResult(match.getPlayerTwoGames() + 1);
            }
            match.setPlayerOnePoints("0");
            match.setPlayerTwoPoints("0");
            match.setPlayerOneGames(0);
            match.setPlayerTwoGames(0);
            match.setTieBreak(false);
        } else {
            if (scoreButtonId == 1) {
                match.setPlayerOnePoints(String.valueOf(playerOnePoints + 1));
            }
            if (scoreButtonId == 2) {
                match.setPlayerTwoPoints(String.valueOf(playerTwoPoints + 1));
            }
        }

    }

    public void countGames(MatchCurrentState match) {
        Integer playerOneSets = match.getPlayerOneSets();
        Integer playerTwoSets = match.getPlayerTwoSets();
        Integer playerOneGames = match.getPlayerOneGames();
        Integer playerTwoGames = match.getPlayerTwoGames();

        if (playerOneGames == 6 && playerTwoGames == 6) {
            match.setTieBreak(true);
            return;
        }
        if (playerOneGames == 6 && ((playerOneGames - playerTwoGames) >= Math.abs(2))) {
            match.setPlayerOneSets(playerOneSets + 1);
            match.setPlayerOneSetsResult(playerOneGames);
            match.setPlayerTwoSetsResult(playerTwoGames);
            countSets(match);
            match.setPlayerOneGames(0);
            match.setPlayerTwoGames(0);
            return;
        }
        if (match.getPlayerTwoGames() == 6 && playerOneGames <= 4) {
            match.setPlayerTwoSets(playerTwoSets + 1);
            match.setPlayerOneSetsResult(playerOneGames);
            match.setPlayerTwoSetsResult(playerTwoGames);
            countSets(match);
            match.setPlayerOneGames(0);
            match.setPlayerTwoGames(0);
            return;
        }
    }

    public void countSets(MatchCurrentState match) {
        if (match.getPlayerOneSets() + match.getPlayerTwoSets() == 3) {
            if (match.getPlayerOneSets() > match.getPlayerTwoSets()) {
                match.setWinnerId(match.getPlayerOneId());
            } else {
                match.setWinnerId(match.getPlayerTwoId());
            }
            match.setMatchFinished(true);
        }
    }
}



