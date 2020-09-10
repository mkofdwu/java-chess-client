package org.example.javachessclient.chess.models;

public abstract class Piece {
    protected Board board;
    protected Square square;
    protected boolean isWhite;

    public Piece(Board board, Square square, boolean isWhite) {
        this.board = board;
        this.square = square;
        this.isWhite = isWhite;
    }

    public boolean getIsWhite() {
        return isWhite;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public abstract String getIconFilePath();

    public abstract boolean canMoveTo(int toFile, int toRank);

    public boolean canMoveTo(Square to) {
        return canMoveTo(to.getFile(), to.getRank());
    }
}
