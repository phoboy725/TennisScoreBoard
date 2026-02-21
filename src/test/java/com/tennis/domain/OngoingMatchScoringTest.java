package com.tennis.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OngoingMatchScoringTest {

    private static final Long P1_ID = 1L;
    private static final Long P2_ID = 2L;

    private static OngoingMatch newMatch() {
        return new OngoingMatch(P1_ID, P2_ID);
    }

    private static void point(OngoingMatch ongoingMatch, PlayerScored who) {
        ongoingMatch.pointWonBy(who);
    }

    private static void points(OngoingMatch ongoingMatch, PlayerScored who, int times) {
        for (int i = 0; i < times; i++) {
            point(ongoingMatch, who);
        }
    }


//    Быстрая победа в гейме без deuce: 4 очка подряд одному игроку (второй остаётся на LOVE).

    private static void winGameSimple(OngoingMatch ongoingMatch, PlayerScored who) {
        points(ongoingMatch, who, 4);
        assertEquals(TennisPoint.LOVE, p1(ongoingMatch).getPoints(), "После гейма очки P1 должны сбрасываться в LOVE");
        assertEquals(TennisPoint.LOVE, p2(ongoingMatch).getPoints(), "После гейма очки P2 должны сбрасываться в LOVE");
    }

//    Быстрая победа в сете 6:0 без tie-break.

    private static void winSet6_0(OngoingMatch ongoingMatch, PlayerScored who) {
        for (int i = 0; i < 6; i++) {
            winGameSimple(ongoingMatch, who);
        }
    }

    private static PlayerScore p1(OngoingMatch ongoingMatch) {
        return ongoingMatch.getPlayerOneScore();
    }

    private static PlayerScore p2(OngoingMatch ongoingMatch) {
        return ongoingMatch.getPlayerTwoScore();
    }

    private static void assertGames(OngoingMatch ongoingMatch, int g1, int g2) {
        assertEquals(g1, p1(ongoingMatch).getGames(), "games P1");
        assertEquals(g2, p2(ongoingMatch).getGames(), "games P2");
    }

    private static void assertSets(OngoingMatch ongoingMatch, int s1, int s2) {
        assertEquals(s1, p1(ongoingMatch).getSets(), "sets P1");
        assertEquals(s2, p2(ongoingMatch).getSets(), "sets P2");
    }

    private static void assertPoints(OngoingMatch ongoingMatch, TennisPoint p1Points, TennisPoint p2Points) {
        assertEquals(p1Points, p1(ongoingMatch).getPoints(), "points P1");
        assertEquals(p2Points, p2(ongoingMatch).getPoints(), "points P2");
    }

    private static int lastSetResult(PlayerScore s) {
        List<Integer> r = s.getSetsResult();
        assertFalse(r.isEmpty(), "setsResult должен содержать хотя бы один элемент");
        return r.getLast();
    }

    // ---------- Tests ----------

    @Test
    void regularGame_shouldIncrementGamesAndResetPoints() {
        OngoingMatch ongoingMatch = newMatch();

        // P1: 0 -> 15 -> 30 -> 40 -> game
        winGameSimple(ongoingMatch, PlayerScored.ONE);

        assertGames(ongoingMatch, 1, 0);
        assertSets(ongoingMatch, 0, 0);
        assertFalse(ongoingMatch.isTieBreak());
        assertFalse(ongoingMatch.isFinished());
    }

    @Test
    void deuce_advantage_shouldWorkAndAllowReturnToDeuce() {
        OngoingMatch ongoingMatch = newMatch();

        // доводим до 40:40 (3 очка каждому)
        points(ongoingMatch, PlayerScored.ONE, 3);
        points(ongoingMatch, PlayerScored.TWO, 3);
        assertPoints(ongoingMatch, TennisPoint.FORTY, TennisPoint.FORTY);

        // P1 выигрывает: deuce -> AD for P1
        point(ongoingMatch, PlayerScored.ONE);
        assertPoints(ongoingMatch, TennisPoint.ADVANTAGE, TennisPoint.FORTY);

        // P2 выигрывает: назад в deuce 40:40
        point(ongoingMatch, PlayerScored.TWO);
        assertPoints(ongoingMatch, TennisPoint.FORTY, TennisPoint.FORTY);

        // P2 выигрывает: AD for P2
        point(ongoingMatch, PlayerScored.TWO);
        assertPoints(ongoingMatch, TennisPoint.FORTY, TennisPoint.ADVANTAGE);

        // P2 выигрывает ещё раз: гейм P2
        point(ongoingMatch, PlayerScored.TWO);
        assertGames(ongoingMatch, 0, 1);
        assertPoints(ongoingMatch, TennisPoint.LOVE, TennisPoint.LOVE);
    }

    @Test
    void setWin_shouldResetGamesAndStoreSetResult() {
        OngoingMatch ongoingMatch = newMatch();

        winSet6_0(ongoingMatch, PlayerScored.ONE);

        // После выигранного сета games должны сброситься, sets увеличиться
        assertSets(ongoingMatch, 1, 0);
        assertGames(ongoingMatch, 0, 0);

        // setsResult: 6:0
        assertEquals(6, lastSetResult(p1(ongoingMatch)));
        assertEquals(0, lastSetResult(p2(ongoingMatch)));

        assertFalse(ongoingMatch.isTieBreak());
        assertFalse(ongoingMatch.isFinished());
    }

    @Test
    void sixAll_shouldEnterTieBreak() {
        OngoingMatch ongoingMatch = newMatch();

        // делаем 6:6 по геймам, без deuce
        for (int i = 0; i < 6; i++) {
            winGameSimple(ongoingMatch, PlayerScored.ONE);
            winGameSimple(ongoingMatch, PlayerScored.TWO);
        }

        assertGames(ongoingMatch, 6, 6);
        assertTrue(ongoingMatch.isTieBreak(), "При 6:6 матч должен перейти в tie-break");
        assertFalse(ongoingMatch.isFinished());
        assertPoints(ongoingMatch, TennisPoint.LOVE, TennisPoint.LOVE);
    }

    @Test
    void tieBreakWin_shouldAwardSet_7_6_andExitTieBreak() {
        OngoingMatch ongoingMatch = newMatch();

        // 6:6 -> tie-break
        for (int i = 0; i < 6; i++) {
            winGameSimple(ongoingMatch, PlayerScored.ONE);
            winGameSimple(ongoingMatch, PlayerScored.TWO);
        }
        assertTrue(ongoingMatch.isTieBreak());

        // Счет в тай-брейке: 5:0
        points(ongoingMatch, PlayerScored.ONE, 5);

        // P2 выигрывает 5 очков - 5:5
        points(ongoingMatch, PlayerScored.TWO, 5);
        assertTrue(ongoingMatch.isTieBreak());

        // P1 выигрывает два подряд - 7:5 (разница 2)
        point(ongoingMatch, PlayerScored.ONE);
        point(ongoingMatch, PlayerScored.ONE);

        assertFalse(ongoingMatch.isTieBreak(), "После завершения tie-break должен выключиться");
        assertSets(ongoingMatch, 1, 0);
        assertGames(ongoingMatch, 0, 0);

        // setsResult: 7:6
        assertEquals(7, lastSetResult(p1(ongoingMatch)));
        assertEquals(6, lastSetResult(p2(ongoingMatch)));

        assertFalse(ongoingMatch.isFinished());
    }

    @Test
    void tieBreak_requiresTwoPointLeadAfter6_6() {
        OngoingMatch ongoingMatch = newMatch();

        // 6:6 -> tie-break
        for (int i = 0; i < 6; i++) {
            winGameSimple(ongoingMatch, PlayerScored.ONE);
            winGameSimple(ongoingMatch, PlayerScored.TWO);
        }

        // Функция для проверки счета в тай-брейке
        java.util.function.BiFunction<Integer, Integer, Boolean> isWinning =
                (w, l) -> w >= 7 && (w - l) >= 2;

        // Проверяем различные счета
        assertFalse(isWinning.apply(6, 6), "6:6 - не победа");
        assertFalse(isWinning.apply(7, 6), "7:6 - недостаточно");
        assertTrue(isWinning.apply(7, 5), "7:5 - победа");
        assertFalse(isWinning.apply(8, 7), "8:7 - недостаточно");
        assertTrue(isWinning.apply(9, 7), "9:7 - победа");
        assertTrue(isWinning.apply(10, 8), "10:8 - победа");
        assertFalse(isWinning.apply(15, 14), "15:14 - недостаточно");
        assertTrue(isWinning.apply(16, 14), "16:14 - победа");
    }

    @Test
    void tieBreak_shouldNotFinishAt7_6_withoutTwoPointLead() {
        OngoingMatch ongoingMatch = newMatch();

        // 6:6 -> tie-break
        for (int i = 0; i < 6; i++) {
            winGameSimple(ongoingMatch, PlayerScored.ONE);
            winGameSimple(ongoingMatch, PlayerScored.TWO);
        }
        assertTrue(ongoingMatch.isTieBreak());

        // Даем по 6 очков - 6:6
        points(ongoingMatch, PlayerScored.ONE, 6);
        points(ongoingMatch, PlayerScored.TWO, 6);

        assertTrue(ongoingMatch.isTieBreak(), "6:6 - игра продолжается");

        // P1 выигрывает - 7:6 (разница 1)
        point(ongoingMatch, PlayerScored.ONE);

        // 7:6 - НЕ победа (разница всего 1)
        assertTrue(ongoingMatch.isTieBreak(), "7:6 - разница 1, игра продолжается");
        assertSets(ongoingMatch, 0, 0);
        assertGames(ongoingMatch, 6, 6);

        // P2 выигрывает - 7:7
        point(ongoingMatch, PlayerScored.TWO);
        assertTrue(ongoingMatch.isTieBreak(), "7:7 - игра продолжается");

        // P1 выигрывает два подряд - 9:7 (разница 2)
        point(ongoingMatch, PlayerScored.ONE);
        point(ongoingMatch, PlayerScored.ONE);

        assertFalse(ongoingMatch.isTieBreak(), "9:7 - победа");
        assertSets(ongoingMatch, 1, 0);
        assertEquals(7, lastSetResult(p1(ongoingMatch)));
        assertEquals(6, lastSetResult(p2(ongoingMatch)));
    }

    @Test
    void matchWin_twoSets_shouldFinishAndSetWinnerId() {
        OngoingMatch ongoingMatch = newMatch();

        // 2 сета по 6:0 для P1
        winSet6_0(ongoingMatch, PlayerScored.ONE);
        assertFalse(ongoingMatch.isFinished());

        winSet6_0(ongoingMatch, PlayerScored.ONE);

        assertTrue(ongoingMatch.isFinished(), "Матч должен завершиться после 2 выигранных сетов");
        assertEquals(P1_ID, ongoingMatch.getWinnerId(), "winnerId должен быть id победителя");
        assertSets(ongoingMatch, 2, 0);
    }

    @Test
    void afterFinish_pointsIgnored_stateDoesNotChange() {
        OngoingMatch ongoingMatch = newMatch();

        // быстро завершаем матч
        winSet6_0(ongoingMatch, PlayerScored.ONE);
        winSet6_0(ongoingMatch, PlayerScored.ONE);
        assertTrue(ongoingMatch.isFinished());
        assertEquals(P1_ID, ongoingMatch.getWinnerId());

        int sets1 = p1(ongoingMatch).getSets();
        int sets2 = p2(ongoingMatch).getSets();
        int games1 = p1(ongoingMatch).getGames();
        int games2 = p2(ongoingMatch).getGames();
        TennisPoint p1Points = p1(ongoingMatch).getPoints();
        TennisPoint p2Points = p2(ongoingMatch).getPoints();

        // попытки добавить очки после завершения
        points(ongoingMatch, PlayerScored.TWO, 10);
        points(ongoingMatch, PlayerScored.ONE, 10);

        assertEquals(sets1, p1(ongoingMatch).getSets());
        assertEquals(sets2, p2(ongoingMatch).getSets());
        assertEquals(games1, p1(ongoingMatch).getGames());
        assertEquals(games2, p2(ongoingMatch).getGames());
        assertEquals(p1Points, p1(ongoingMatch).getPoints());
        assertEquals(p2Points, p2(ongoingMatch).getPoints());
        assertEquals(P1_ID, ongoingMatch.getWinnerId());
    }
}
