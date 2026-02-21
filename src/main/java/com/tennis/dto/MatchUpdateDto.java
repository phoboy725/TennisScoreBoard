package com.tennis.dto;

import com.tennis.domain.OngoingMatch;

import java.util.UUID;

public record MatchUpdateDto(
        UUID uuid,
        OngoingMatch ongoingMatch
) {
}
