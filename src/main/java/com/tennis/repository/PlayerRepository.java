package com.tennis.repository;

import com.tennis.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Player findPlayerById(Long id);

    Optional<Player> findPlayerByName(String name);

    Player save(Player player);
}
