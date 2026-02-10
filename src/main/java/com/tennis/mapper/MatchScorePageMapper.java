package com.tennis.mapper;

import com.tennis.domain.OngoingMatch;
import com.tennis.dto.MatchScorePageDto;
import com.tennis.dto.MatchStateDto;

public final class MatchScorePageMapper {

    private MatchScorePageMapper() {}

    public static MatchScorePageDto toPageDto(
            String uuid,
            String playerOneName,
            String playerTwoName,
            OngoingMatch match
    ) {
        MatchStateDto state = MatchStateMapper.toDto(match);

        return new MatchScorePageDto(
                uuid,
                playerOneName,
                playerTwoName,
                state
        );
    }
}

