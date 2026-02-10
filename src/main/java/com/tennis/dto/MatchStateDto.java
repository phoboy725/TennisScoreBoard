package com.tennis.dto;

public record MatchStateDto(
        PlayerScoreDto playerOneScore,
        PlayerScoreDto playerTwoScore,
        boolean matchFinished,
        boolean tieBreak,
        Long winnerId
) {
}
