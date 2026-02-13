package com.tennis.validation;

import com.tennis.config.ApplicationContext;
import com.tennis.service.PlayerService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PlayerNamesValidation {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[а-яА-яa-zA-Z'\\-\\s]{2,30}$");
    private static final String EMPTY_NAME = "Names can not be empty";
    private static final String NOT_UNIQUE_NAME = "Names must be unique";
    private static final String EXISTING_NAME = "Player with this name already exists: ";
    private static final String WRONG_CHARS_NAME  = "Names must be 2 to 30 characters long, using only letters, \" ' \", \" - \" and space";

    private static PlayerService playerService = ApplicationContext.playerService();

    public static List<String> check(String playerOneName, String playerTwoName) {
        List<String> errors = new ArrayList<>();

        if (playerOneName == null || playerTwoName == null) {
            errors.add(EMPTY_NAME);
        }

        String playerOneNameStripped = playerOneName.strip();
        String playerTwoNameStripped = playerTwoName.strip();

        if (playerOneNameStripped.isEmpty() || playerTwoNameStripped.isEmpty()) {
            errors.add(EMPTY_NAME);
        }
        if (playerOneNameStripped.equalsIgnoreCase(playerTwoNameStripped)) {
            errors.add(NOT_UNIQUE_NAME);
        }
        if (!NAME_PATTERN.matcher(playerOneNameStripped).matches() || !NAME_PATTERN.matcher(playerTwoNameStripped).matches()) {
            errors.add(WRONG_CHARS_NAME);
        }
        if (!playerService.findPlayerByName(playerOneNameStripped).isEmpty()) {
            errors.add(EXISTING_NAME + playerOneNameStripped);
        }
        if (!playerService.findPlayerByName(playerTwoNameStripped).isEmpty()) {
            errors.add(EXISTING_NAME + playerTwoNameStripped);
        }
        return errors;
    }
}
