package com.tennis.dto;

import com.tennis.domain.OngoingMatch;

public record MatchUpdateDto(
        OngoingMatch ongoingMatch,
        boolean finished
) {
}
