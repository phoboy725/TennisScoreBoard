package com.tennis.dto;

public record MatchScorePageDto(
        String uuid,
        String playerOneName,
        String playerTwoName,
        MatchStateDto state
) {}

