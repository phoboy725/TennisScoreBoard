package com.tennis.mapper;

import com.tennis.dto.PlayerRequestDto;
import com.tennis.entity.Player;

public enum CreatePlayerMapper {
    INSTANCE;

    public static Player mapFrom(PlayerRequestDto object) {
        return new Player(object.name());
    }
}

