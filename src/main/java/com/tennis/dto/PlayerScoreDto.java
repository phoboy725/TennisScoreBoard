package com.tennis.dto;

import com.tennis.domain.PlayerScore;

import java.util.ArrayList;
import java.util.List;


public record PlayerScoreDto(
        Long id,
        String points,
        int games,
        int sets,
        int tieBreakPoints,
        List<Integer> setsResult
) {
    public static PlayerScoreDto from(PlayerScore score) {
        return new PlayerScoreDto(
                score.getId(),
                score.getPoints().getDisplayValue(),
                score.getGames(),
                score.getSets(),
                score.getTieBreakPoints(),
                new ArrayList<>(score.getSetsResult())
        );
    }

    public String displayPoints() {
        return tieBreakPoints > 0 ? String.valueOf(tieBreakPoints) : points;
    }

    public String getSetScore(int index) {
        if (setsResult == null
                || index < 0
                || index >= setsResult.size()
                || setsResult.get(index) == null) {
            return "-";
        }
        return String.valueOf(setsResult.get(index));
    }

}

