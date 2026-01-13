package com.tennis.validation;

import java.util.regex.Pattern;

public class PlayerNamesValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[а-яА-яa-zA-Z'\\-\\s]{2,30}$");

    public static String check(String playerOneName, String playerTwoName) {
        if (playerOneName == null || playerTwoName == null) {
            return "Names must be 2 to 30 characters long, using only letters, \" ' \", \" - \" and space";
        }
        if (playerOneName.trim().isEmpty() || playerTwoName.trim().isEmpty()) {
            return "Names must be 2 to 30 characters long, using only letters, \" ' \", \" - \" and space";
        }
        if (playerOneName.equalsIgnoreCase(playerTwoName)) {
            return "Names must be unique";
        }
        if (!NAME_PATTERN.matcher(playerOneName).matches() || !NAME_PATTERN.matcher(playerTwoName).matches()) {
            return "Names must be 2 to 30 characters long, using only letters, \" ' \", \" - \" and space";
        }
        return null;
    }
}
