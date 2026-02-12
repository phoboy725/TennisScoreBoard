package com.tennis.dto;

import com.tennis.domain.MatchState;

import java.util.UUID;

public record MatchResponseDto(
        UUID uuid,
        PlayerInfoDto playerOne,
        PlayerInfoDto playerTwo,
        PlayerScoreDto playerOneScore,
        PlayerScoreDto playerTwoScore,
        MatchState matchState,
        PlayerInfoDto winner
) {
}
