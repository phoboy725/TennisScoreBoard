package com.tennis.dto;

import com.tennis.domain.MatchState;

public record OngoingMatchDto(
        PlayerInfoDto playerOne,
        PlayerInfoDto playerTwo,
        PlayerScoreDto playerOneScore,
        PlayerScoreDto playerTwoScore,
        MatchState matchState,
        PlayerInfoDto winner
) {
}
