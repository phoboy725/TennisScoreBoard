package com.tennis.dto;

import com.tennis.domain.OngoingMatch;
import com.tennis.entity.Match;
import com.tennis.entity.Player;
import com.tennis.service.FinishedMatchService;
import com.tennis.service.OngoingMatchService;
import com.tennis.service.PlayerService;

public class MatchDtoFactory {

    private final OngoingMatchService ongoingMatchService;
    private final FinishedMatchService finishedMatchService;
    private final PlayerService playerService;

    public MatchDtoFactory(OngoingMatchService ongoingMatchService, FinishedMatchService finishedMatchService, PlayerService playerService) {
        this.ongoingMatchService = ongoingMatchService;
        this.finishedMatchService = finishedMatchService;
        this.playerService = playerService;
    }

    public OngoingMatchDto fromMatch(OngoingMatch match) {

        Player playerOne = (playerService.findPlayerById(match.getPlayerOneScore().getId())).get();
        Player playerTwo = (playerService.findPlayerById(match.getPlayerTwoScore().getId())).get();
        Player winner = (playerOne.getId() == match.getWinnerId()) ? playerOne : playerTwo;

        return new OngoingMatchDto(
                PlayerInfoDto.from(playerOne),
                PlayerInfoDto.from(playerTwo),
                PlayerScoreDto.from(match.getPlayerOneScore()),
                PlayerScoreDto.from(match.getPlayerTwoScore()),
                match.getState(),
                PlayerInfoDto.from(winner)
        );
    }

    public FinishedMatchDto from(Match match) {
        return new FinishedMatchDto(
                PlayerInfoDto.from(match.getPlayerOne()),
                PlayerInfoDto.from(match.getPlayerTwo()),
                PlayerInfoDto.from(match.getWinner())
        );
    }
}
