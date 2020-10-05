package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public abstract class Piece {
    protected Chess chess;
    protected Square square;
    protected boolean isWhite;

    public Piece(Chess chess, Square square, boolean isWhite) {
        this.chess = chess;
        this.square = square;
        this.isWhite = isWhite;
    }

    public Chess getChess() {
        return chess;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public boolean getIsWhite() {
        return isWhite;
    }

    public abstract String getIconFilePath();

    public abstract ArrayList<Move> findAvailableMoves(); // checks everything, including special moves & leaving king in check

    public abstract boolean isAttackingSquare(Square otherSquare);

    public abstract Square makeSpecialMoveAndGetAffectedSquare(Move move); // modifies the board according to the special move

    public abstract void undoSpecialMove(Move move);

    @Override
    public boolean equals(Object other) {
        return other instanceof Piece && square.equals(((Piece) other).getSquare()) && isWhite == ((Piece) other).getIsWhite();
    }

    @Override
    public String toString() {
        return (isWhite ? "white" : "black") + " " + getClass().getSimpleName() + " on " + square;
    }
}
