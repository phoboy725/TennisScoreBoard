package com.tennis.dto;

import com.tennis.model.Player;

public record MatchRequestDto(Player playerOne, Player playerTwo, Player winner) {
}
