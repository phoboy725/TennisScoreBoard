package com.tennis.dto;

import com.tennis.domain.PlayerScored;

import java.util.UUID;

public record MatchRequestDto(
        UUID matchId,
        PlayerScored playerScored
) {
}
