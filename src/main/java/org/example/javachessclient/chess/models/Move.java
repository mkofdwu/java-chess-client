package org.example.javachessclient.chess.models;

import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.pieces.Piece;

public class Move {
    private final Piece piece;
    private final Square square;
    private final MoveType type;

    public Move(Piece piece, Square square, MoveType type) {
        this.piece = piece;
        this.square = square;
        this.type = type;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getSquare() {
        return square;
    }

    public MoveType getType() {
        return type;
    }
}
