package com.tennis.service;

import com.tennis.dto.PlayerRequestDto;
import com.tennis.mapper.CreatePlayerMapper;
import com.tennis.entity.Player;
import com.tennis.repositories.PlayerDao;

public class PlayerService {

    private PlayerDao playerDao;

    public PlayerService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public Player getOrCreatePlayer(String playerName) {
        Player player = playerDao.getPlayerByName(playerName);
        if (player == null) {
            PlayerRequestDto playerRequestDto = new PlayerRequestDto(playerName);
            player = CreatePlayerMapper.INSTANCE.mapFrom(playerRequestDto);
            playerDao.createPlayer(player);
        }
        return player;
    }
}
