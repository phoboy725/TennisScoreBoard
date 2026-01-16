package com.tennis.dto;

import java.util.List;

public record PlayerScoreDto(
        Long id,
        int sets,
        int games,
        String points,
        List<Integer> setsResult
) { }
