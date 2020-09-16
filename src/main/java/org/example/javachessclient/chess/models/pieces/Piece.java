package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.AvailableMove;
import org.example.javachessclient.chess.Board;
import org.example.javachessclient.chess.Square;

import java.util.ArrayList;

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

    public abstract ArrayList<AvailableMove> availableMoves(); // checks everything, including special moves

    public abstract void makeSpecialMove(AvailableMove move); // modifies the board according to the special move
}
