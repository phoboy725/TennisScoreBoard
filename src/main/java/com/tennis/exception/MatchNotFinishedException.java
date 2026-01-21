package com.tennis.exception;

public class MatchNotFinishedException extends RuntimeException {
    public MatchNotFinishedException(String message) {
        super(message);
    }

    public MatchNotFinishedException(String message, Throwable cause) {super(message, cause);}
}
