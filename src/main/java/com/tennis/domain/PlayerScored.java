package com.tennis.domain;

public enum PlayerScored {
    ONE("ONE"),
    TWO("TWO");

    private final String code;

    PlayerScored(String code) {
        this.code = code;
    }

    public static PlayerScored from(String value) {
        for (PlayerScored ps : values()) {
            if (ps.code.equalsIgnoreCase(value)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("Unknown scoreButtonId: " + value);
    }
}
