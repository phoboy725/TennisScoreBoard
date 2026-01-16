package com.tennis.dto;

public record MatchStateDto(
        PlayerScoreDto playerOneScore,
        PlayerScoreDto playerTwoScore,
        Long winnerId
) {
}
