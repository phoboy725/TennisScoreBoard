package com.tennis.dto;

import com.tennis.entity.Match;

public record FinishedMatchDto(
        PlayerInfoDto playerOne,
        PlayerInfoDto playerTwo,
        PlayerInfoDto winner
) {
}
