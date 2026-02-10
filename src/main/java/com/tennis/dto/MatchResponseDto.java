package com.tennis.dto;

import com.tennis.domain.MatchState;

import java.util.UUID;

public record MatchResponseDto(
        UUID uuid,
        PlayerScoreDto playerOne,
        PlayerScoreDto playerTwo,
        MatchState matchState,
        Long winnerId
) {
}
