package com.tennis.dto;

public record FinishedMatchDto(
        PlayerInfoDto playerOne,
        PlayerInfoDto playerTwo,
        PlayerInfoDto winner
) {
}
