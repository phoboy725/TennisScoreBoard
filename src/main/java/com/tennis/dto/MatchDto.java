package com.tennis.dto;

import java.util.UUID;

public record MatchDto(
        UUID id,
        String firstPlayerName,
        String secondPlayerName,
        ScoreDto firstPlayerScore,
        ScoreDto secondPlayerScore,
        String winnerName,
        boolean finished
) {
}
