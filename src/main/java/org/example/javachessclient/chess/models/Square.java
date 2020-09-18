package org.example.javachessclient.chess.models;

public class Square {
    private final int file;
    private final int rank;

    public Square(int file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public Square(String square) { // square using standard notation, (e.g. e3, h6)
        file = "abcdefgh".indexOf(square.charAt(0));
        rank = Character.digit(square.charAt(1), 10);
    }

    public boolean isWhite() {
        return (rank % 2 ^ file % 2) == 0;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "abcdefgh".charAt(file) + Integer.toString(rank);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Square
                && file == ((Square) other).getFile()
                && rank == ((Square) other).getRank();
    }
}
