package org.example.javachessclient.chess.exceptions;

public class BadSquare extends RuntimeException {
    public BadSquare(String message) {
        super(message);
    }
}
