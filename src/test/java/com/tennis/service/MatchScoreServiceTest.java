package com.tennis.service;

import com.tennis.dto.MatchCurrentState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchScoreServiceTest {

    private final MatchScoreService service = MatchScoreService.getInstance();

    @Test
    void player1WinsPointAt40_40_gameDoesNotEnd() {
        MatchCurrentState match = new MatchCurrentState(1L,2L);
        match.setPlayerOnePoints("40");
        match.setPlayerTwoPoints("40");

        service.countPoints(match, 1); // игрок 1 выигрывает очко

        assertEquals("AD", match.getPlayerOnePoints());
        assertEquals("40", match.getPlayerTwoPoints());
        // гейм не завершён, Games не увеличились
        assertEquals(0, match.getPlayerOneGames());
        assertEquals(0, match.getPlayerTwoGames());
    }

    @Test
    void player2WinsPointWhenPlayer1inAdvantage_Player1PointsReturnTo40() {
        MatchCurrentState match = new MatchCurrentState(1L,2L);
        match.setPlayerOnePoints("AD");
        match.setPlayerTwoPoints("40");

        service.countPoints(match, 2); // игрок 2 выигрывает очко

        assertEquals("40", match.getPlayerOnePoints());
        assertEquals("40", match.getPlayerTwoPoints());

    }

    @Test
    void player1WinsPointAt40_0_winsGame() {
        MatchCurrentState match = new MatchCurrentState(1L,2L);
        match.setPlayerOnePoints("40");
        match.setPlayerTwoPoints("0");

        service.countPoints(match, 1); // игрок 1 выигрывает очко

        assertEquals("0", match.getPlayerOnePoints());
        assertEquals("0", match.getPlayerTwoPoints());
        assertEquals(1, match.getPlayerOneGames()); // гейм +1
        assertEquals(0, match.getPlayerTwoGames());
    }

    @Test
    void tieBreakStartsAt6_6() {
        MatchCurrentState match = new MatchCurrentState(1L,2L);
        match.setPlayerOneGames(6);
        match.setPlayerTwoGames(6);

        service.countGames(match);

        assertTrue(match.isTieBreak());
    }

    @Test
    void tieBreakPlayer1Wins7_5() {
        MatchCurrentState match = new MatchCurrentState(1L,2L);
        match.setPlayerOneGames(6);
        match.setPlayerTwoGames(6);
        match.setTieBreak(true);
        match.setPlayerOnePoints("6");
        match.setPlayerTwoPoints("5");

        service.countTieBreak(match, 1); // 7–5

        assertEquals(1, match.getPlayerOneSets());
        assertEquals(0, match.getPlayerTwoSets());
        assertFalse(match.isTieBreak());
        assertEquals(0, match.getPlayerOneGames());
        assertEquals(0, match.getPlayerTwoGames());
    }

    @Test
    void playerWinsMatchWhenSetsEquals2() {

        MatchCurrentState match = new MatchCurrentState(1L,2L);
        match.setPlayerOneSets(1);
        match.setPlayerTwoSets(1);

        service.incrementSets(match, 1);          // стало 2:1 по сетам
        service.checkAndFinishMatch(match);       // проверка и установка флага

        assertTrue(match.isMatchFinished());
        assertEquals(match.getPlayerOneId(), match.getWinnerId());

    }

}

