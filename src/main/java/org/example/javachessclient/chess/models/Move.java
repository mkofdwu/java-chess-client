package org.example.javachessclient.chess.models;

import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.pieces.Piece;
import org.example.javachessclient.socketgame.models.SocketMove;

public class Move {
    private final Piece piece;
    private final Square fromSquare;
    private final Square toSquare;
    private final MoveType type;
    private final Piece capturedPiece;

    public Move(Piece piece, Square fromSquare, Square toSquare, MoveType type, Piece capturedPiece) {
        this.piece = piece;
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.type = type;
        this.capturedPiece = capturedPiece;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public MoveType getType() {
        return type;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }
}
