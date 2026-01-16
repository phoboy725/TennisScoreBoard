package com.tennis.service;

import com.tennis.entity.Player;
import com.tennis.repository.PlayerRepository;

public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getOrCreatePlayer(String playerName) {
        Player foundPlayer = playerRepository.findPlayerByName(playerName);
        if (foundPlayer != null) {
            return foundPlayer;
        }
        Player newPlayer = new Player(playerName);
        return playerRepository.save(newPlayer);
    }
}
