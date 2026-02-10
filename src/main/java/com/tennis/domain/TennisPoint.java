package com.tennis.domain;

public enum TennisPoint {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String displayValue;

    TennisPoint(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public TennisPoint next() {
        if (this == ADVANTAGE) {
            throw new IllegalStateException("Has no points after advantage.");
        }
        return values()[this.ordinal() + 1];
    }
}
