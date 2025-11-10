package com.tennis.service;

import com.tennis.dto.MatchCurrentState;

public class MatchScoreService {

    private static final MatchScoreService INSTANCE = new MatchScoreService();

    public static MatchScoreService getInstance() {
        return INSTANCE;
    }

    private void countDispatch(MatchCurrentState match, Integer scoreButtonId) {
        if (match.isMatchFinished())
            if (match.isDeuce()) {
                countPoints(    match, scoreButtonId);
            }
    }

    private String convertPointsToTennis(Integer from) {
        var to = switch (from) {
            case 0 -> "0";
            case 1 -> "15";
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

    public void countGames(MatchCurrentState match) {
        Integer playerOneSets = match.getPlayerOneSets();
        Integer playerTwoSets = match.getPlayerTwoSets();
        if (match.getPlayerOneGames() == 6) {
            match.setPlayerOneSets(playerOneSets + 1);
            match.setPlayerOneSetsResult(match.getPlayerOneGames());
            match.setPlayerTwoSetsResult(match.getPlayerTwoGames());
            System.out.println(match.getPlayerOneSetsResultFull());
            System.out.println(match.getPlayerTwoSetsResultFull());
            countSets(match);
            match.setPlayerOneGames(0);
            match.setPlayerTwoGames(0);
            return;
        }
        if (match.getPlayerTwoGames() == 6) {
            match.setPlayerTwoSets(playerTwoSets + 1);
            match.setPlayerOneSetsResult(match.getPlayerOneGames());
            match.setPlayerTwoSetsResult(match.getPlayerTwoGames());
            System.out.println(match.getPlayerOneSetsResultFull());
            System.out.println(match.getPlayerTwoSetsResultFull());
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



