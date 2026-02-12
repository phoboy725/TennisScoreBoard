package com.tennis.dto;

import com.tennis.config.ApplicationContext;
import com.tennis.domain.OngoingMatch;
import com.tennis.domain.PlayerScore;
import com.tennis.entity.Match;
import com.tennis.entity.Player;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;

import java.util.List;
import java.util.UUID;

public class MatchDtoFactory {

    private final OngoingMatchService ongoingMatchService = ApplicationContext.ongoingMatchService();
    private final PlayerService playerService = ApplicationContext.playerService();

    public MatchResponseDto fromMatch(UUID uuid) {

        OngoingMatch match = ongoingMatchService.findMatch(uuid);
        Player playerOne = playerService.findPlayerById(match.getPlayerOneScore().getId());
        Player playerTwo = playerService.findPlayerById(match.getPlayerTwoScore().getId());
        Player winner = (playerOne.getId() == match.getWinnerId()) ? playerOne : playerTwo;

        return new MatchResponseDto(
                uuid,
                PlayerInfoDto.from(playerOne),
                PlayerInfoDto.from(playerTwo),
                PlayerScoreDto.from(match.getPlayerOneScore()),
                PlayerScoreDto.from(match.getPlayerTwoScore()),
                match.getState(),
                PlayerInfoDto.from(winner)
        );
    }

    public static FinishedMatchDto from(Match match) {
        return new FinishedMatchDto(
                PlayerInfoDto.from(match.getPlayerOne()),
                PlayerInfoDto.from(match.getPlayerTwo()),
                PlayerInfoDto.from(match.getWinner())
        );
    }
}
