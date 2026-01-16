package com.tennis.repository;

import com.tennis.entity.Player;

public interface PlayerRepository {
    Player findPlayerById(Long id);

    Player findPlayerByName(String name);

    Player save(Player player);
}
