package com.tennis.repository;

import com.tennis.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findById(Long id);

    Optional<Player> findByName(String name);

    Player save(Player player);
}
