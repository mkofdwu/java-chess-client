package org.example.javachessclient.chess.models;

import org.example.javachessclient.chess.exceptions.BadSquare;

public class Square {
    private final int file;
    private final int rank;

    public Square(int file, int rank) {
        if (file < 0 || 7 < file || rank < 0 || 7 < rank) {
            throw new BadSquare("bad square at file " + file + " and rank " + rank);
        }
        this.file = file;
        this.rank = rank;
    }

    public Square(String square) { // square using standard notation, (e.g. e3, h6)
        file = "abcdefgh".indexOf(square.charAt(0));
        rank = 8 - Character.digit(square.charAt(1), 10);
    }

    public static boolean isValid(int file, int rank) {
        return 0 <= file && file <= 7 && 0 <= rank && rank <= 7;
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
        return "abcdefgh".charAt(file) + Integer.toString(8 - rank);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Square
                && file == ((Square) other).getFile()
                && rank == ((Square) other).getRank();
    }
}
