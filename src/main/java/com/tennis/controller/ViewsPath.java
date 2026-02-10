package com.tennis.controller;

public enum ViewsPath {
    NEW_MATCH("new-match"),
    MATCH_SCORE("match-score"),
    MATCH_WINNER("match-winner"),
    MATCHES("matches");

    private final String jsp;

    ViewsPath(String jsp) {
        this.jsp = jsp;
    }

    public String jsp() { return jsp; }

}
