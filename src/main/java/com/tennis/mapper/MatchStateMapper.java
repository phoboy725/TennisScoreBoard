package com.tennis.mapper;

import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScore;
import com.tennis.dto.MatchStateDto;
import com.tennis.dto.PlayerScoreDto;

public final class MatchStateMapper {

    private MatchStateMapper() {}

    public static MatchStateDto toDto(OngoingMatch match) {
        return new MatchStateDto(
                toPlayerDto(match.getPlayerOneScoreView()),
                toPlayerDto(match.getPlayerTwoScoreView()),
                match.isFinished(),
                match.isTieBreak(),
                match.getWinnerId()
        );
    }

    private static PlayerScoreDto toPlayerDto(PlayerScore s) {
        return new PlayerScoreDto(
                s.getId(),
                s.getSets(),
                s.getGames(),
                s.getPointsDisplay(),
                s.getTieBreakPoints(),
                s.getSetsResult()
        );
    }
}

