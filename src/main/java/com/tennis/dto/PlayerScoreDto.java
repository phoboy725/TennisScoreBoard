package com.tennis.dto;

import java.util.List;

public record PlayerScoreDto(
        Long id,
        int sets,
        int games,
        String points,
        int tieBreakPoints,
        List<Integer> setsResult
) { }
