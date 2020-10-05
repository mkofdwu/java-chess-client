package org.example.javachessclient.chess.exceptions;

public class InvalidBoardException extends RuntimeException {
    public InvalidBoardException() {
    }

    public InvalidBoardException(String message) {
        super(message);
    }
}
