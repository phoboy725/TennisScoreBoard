package com.tennis.mapper;

import com.tennis.dto.MatchRequestDto;
import com.tennis.dto.PlayerRequestDto;
import com.tennis.model.Match;
import com.tennis.model.Player;

public enum CreateMatchMapper {
    INSTANCE;

    public Match mapFrom(MatchRequestDto object) {
        return new Match(object.playerOne(),
                            object.playerTwo(),
                            object.winner());
    }
}
