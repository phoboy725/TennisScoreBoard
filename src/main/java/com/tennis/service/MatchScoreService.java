package com.tennis.service;

import com.tennis.dto.MatchCurrentState;

import java.util.Map;

public class MatchScoreService {

    private static final MatchScoreService INSTANCE = new MatchScoreService();

    // Константы для теннисных очков
    private static final String LOVE = "0";
    private static final String FIFTEEN = "15";
    private static final String THIRTY = "30";
    private static final String FORTY = "40";
    private static final String ADV = "AD";

    // Теннисные "очки" -> обычные числа
    private static final Map<String, Integer> tennisToInt = Map.of(
            LOVE, 0,
            FIFTEEN, 1,
            THIRTY, 2,
            FORTY, 3,
            ADV, 4
    );

    // Обычные числа -> "теннисные" очки
    private static final String[] intToTennis = {LOVE, FIFTEEN, THIRTY, FORTY, ADV};

    public static MatchScoreService getInstance() {
        return INSTANCE;
    }

    // Преобразование из "теннисных" очков в числа
    private static int convertPointsFromTennis(String points) {
        return tennisToInt.getOrDefault(points, 0);
    }

    // Преобразование из числа в "теннисные" очки
    private static String convertPointsToTennis(int points) {
        return intToTennis[Math.min(points, intToTennis.length - 1)];
    }

    // Подсчет Points
    public void countPoints(MatchCurrentState match, Integer scoreButtonId) {
        int playerOnePoints = convertPointsFromTennis(match.getPlayerOnePoints());
        int playerTwoPoints = convertPointsFromTennis(match.getPlayerTwoPoints());
        if (playerOnePoints == 3 && playerTwoPoints == 3) {
            countDeuce(match, scoreButtonId);
        } else if (playerOnePoints == 3 && playerTwoPoints == 4) {
            countAdvantage(match, scoreButtonId, 2, 1);
        } else if (playerOnePoints == 4 && playerTwoPoints == 3) {
            countAdvantage(match, scoreButtonId, 1, 2);
        } else if ((playerOnePoints == 3 && scoreButtonId == 1) || (playerTwoPoints == 3 && scoreButtonId == 2)) {
            winGameAndResetPoints(match, scoreButtonId);
        } else {
            incrementPoints(match, scoreButtonId);
        }
    }

    // Подсчет Games
    public void countGames(MatchCurrentState match) {
        Integer playerOneGames = match.getPlayerOneGames();
        Integer playerTwoGames = match.getPlayerTwoGames();
        if (playerOneGames == 6 && playerTwoGames == 6) {
            match.setTieBreak(true);
        } else if (playerOneGames >= 6 && ((playerOneGames - playerTwoGames) >= 2)) {
            winSetAndResetGames(match, 1, playerOneGames, playerTwoGames);
        } else if (playerTwoGames >= 6 && ((playerTwoGames - playerOneGames) >= 2)) {
            winSetAndResetGames(match, 2, playerOneGames, playerTwoGames);
        }
    }

    // Подсчет Deuce
    public void countDeuce(MatchCurrentState match, Integer scoreButtonId) {
        if (scoreButtonId == 1) {
            match.setPlayerOnePoints(ADV);
        }
        if (scoreButtonId == 2) {
            match.setPlayerTwoPoints(ADV);
        }
    }

    // Подсчет Advantage
    public void countAdvantage(MatchCurrentState match, Integer scoreButtonId, Integer advPlayerId, Integer otherPlayerId) {
        if (scoreButtonId == advPlayerId) {
            winGameAndResetPoints(match, scoreButtonId);
        } else if (scoreButtonId == otherPlayerId) {
            match.setPlayerOnePoints(FORTY);
            match.setPlayerTwoPoints(FORTY);
        }
    }

    // Инкремент Points
    public void incrementPoints(MatchCurrentState match, int playerWonPoint) {
        int playerOnePoints = convertPointsFromTennis(match.getPlayerOnePoints());
        int playerTwoPoints = convertPointsFromTennis(match.getPlayerTwoPoints());
        if (playerWonPoint == 1) {
            match.setPlayerOnePoints(convertPointsToTennis(playerOnePoints + 1));
        }
        if (playerWonPoint == 2) {
            match.setPlayerTwoPoints(convertPointsToTennis(playerTwoPoints + 1));
        }
    }

    // Инкремент Games
    public void incrementGames(MatchCurrentState match, int playerWonGame) {
        if (playerWonGame == 1) {
            match.setPlayerOneGames(match.getPlayerOneGames() + 1);
        }
        if (playerWonGame == 2) {
            match.setPlayerTwoGames(match.getPlayerTwoGames() + 1);
        }
        countGames(match);
    }

    // Инкремент Sets
    public void incrementSets(MatchCurrentState match, int playerWonGame) {
        if (playerWonGame == 1) {
            match.setPlayerOneSets(match.getPlayerOneSets() + 1);
        }
        if (playerWonGame == 2) {
            match.setPlayerTwoSets(match.getPlayerTwoSets() + 1);
        }
    }

    // Проверка заершения Матча
    public void checkAndFinishMatch(MatchCurrentState match) {
        int playerOneSets = match.getPlayerOneSets();
        int playerTwoSets = match.getPlayerTwoSets();

        if (playerOneSets == 2 || playerTwoSets == 2) {
            if (playerOneSets > playerTwoSets) {
                match.setWinnerId(match.getPlayerOneId());
            } else {
                match.setWinnerId(match.getPlayerTwoId());
            }
            match.setMatchFinished(true);
        }
    }

    // Сохранение результатов сетов
    public void saveSetsResults(MatchCurrentState match, int setResult, int playerId) {
        if (playerId == 1) {
            match.setPlayerOneSetsResult(setResult);
        }
        if (playerId == 2) {
            match.setPlayerTwoSetsResult(setResult);
        }
    }

    // Обнуление счета
    public void resetScore(MatchCurrentState match, String gameStage) {
        if (gameStage.equalsIgnoreCase("points")) {
            match.setPlayerOnePoints("0");
            match.setPlayerTwoPoints("0");
            return;
        }
        if (gameStage.equalsIgnoreCase("games")) {
            match.setPlayerOneGames(0);
            match.setPlayerTwoGames(0);
        }
    }

    // Победа в гейме (Games + 1) и обнуление Points
    public void winGameAndResetPoints(MatchCurrentState match, Integer scoreButtonId) {
        if (scoreButtonId == 1) {
            incrementGames(match, 1);
        } else if (scoreButtonId == 2) {
            incrementGames(match, 2);
        }
        resetScore(match, "points");
    }

    // Победа в сете (Sets + 1) и обнуление Games
    public void winSetAndResetGames(MatchCurrentState match, Integer playerScored, Integer playerOneGames, Integer playerTwoGames) {
        if (playerScored == 1) {
            incrementSets(match, 1);
        } else if (playerScored == 2) {
            incrementSets(match, 2);
        }
        saveSetsResults(match, playerOneGames, 1);
        saveSetsResults(match, playerTwoGames, 2);
        resetScore(match, "games");
        checkAndFinishMatch(match);
    }

    //  Подсчет Tie-Break
    public void countTieBreak(MatchCurrentState match, Integer scoreButtonId) {
        int playerOnePoints = Integer.parseInt(match.getPlayerOnePoints());
        int playerTwoPoints = Integer.parseInt(match.getPlayerTwoPoints());
        if ((playerOnePoints >= 7 || playerTwoPoints >= 7) && (Math.abs(playerOnePoints - playerTwoPoints) >= 2)) {
            if (playerOnePoints > playerTwoPoints) {
                incrementSets(match, 1);
                saveSetsResults(match, 7, 1);
                saveSetsResults(match, 6, 2);
            } else {
                incrementSets(match, 2);
                saveSetsResults(match, 7, 2);
                saveSetsResults(match, 6, 1);
            }
            resetScore(match, "points");
            resetScore(match, "games");
            match.setTieBreak(false);
            checkAndFinishMatch(match);
        } else {
            if (scoreButtonId == 1) {
                match.setPlayerOnePoints(String.valueOf(playerOnePoints + 1));
            }
            if (scoreButtonId == 2) {
                match.setPlayerTwoPoints(String.valueOf(playerTwoPoints + 1));
            }
        }
    }
}



