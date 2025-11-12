package com.tennis.enums;

public enum TennisPoints {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADV("AD");

    private final String display;

    TennisPoints(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public static TennisPoints fromString(String str) {
        for (TennisPoints point : values()) {
            if (point.display.equals(str)) {
                return point;
            }
        }
        throw new IllegalArgumentException("Invalid tennis point: " + str);
    }
}

