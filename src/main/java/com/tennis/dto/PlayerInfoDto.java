package com.tennis.dto;

import com.tennis.entity.Player;

public record PlayerInfoDto(
        Long id,
        String name)
{
    public static PlayerInfoDto from(Player player) {
        return new PlayerInfoDto(player.getId(), player.getName());
    }
}
