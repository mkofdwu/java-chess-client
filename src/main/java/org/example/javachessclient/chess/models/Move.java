package org.example.javachessclient.chess.models;

import org.example.javachessclient.chess.models.pieces.*;
import org.example.javachessclient.chess.models.specialmoves.Castling;
import org.example.javachessclient.chess.models.specialmoves.SpecialEffect;

public class Move {
    private final Piece piece;
    private final Square fromSquare;
    private final Square toSquare;
    private final Piece capturedPiece;
    private final SpecialEffect specialEffect;
    // only set after move is made, used for recording purposes only
    private Piece promotedPiece;
    private boolean checksOpponentKing;

    public Move(Piece piece, Square fromSquare, Square toSquare, Piece capturedPiece, SpecialEffect specialEffect) {
        this.piece = piece;
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.specialEffect = specialEffect;
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

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public SpecialEffect getSpecialEffect() {
        return specialEffect;
    }

    public void setPromotedPiece(Piece promotedPiece) {
        this.promotedPiece = promotedPiece;
    }

    public boolean getChecksOpponentKing() {
        return checksOpponentKing;
    }

    public void setChecksOpponentKing(boolean checksOpponentKing) {
        this.checksOpponentKing = checksOpponentKing;
    }

    @Override
    public String toString() {
        // format move in chess notation
        String formattedMove;
        if (specialEffect instanceof Castling) {
            formattedMove = toSquare.getFile() == 2 ? "O-O-O" : "O-O";
        } else {
            formattedMove = getPieceAbbr(piece)
                    + specifyFileOrRank()
                    + (capturedPiece == null ? "" : "x")
                    + toSquare
                    + (promotedPiece == null ? "" : "=" + getPieceAbbr(promotedPiece));
        }
        return formattedMove + (checksOpponentKing ? "+" : "");
    }

    private static String getPieceAbbr(Piece piece) { // fixme: improve & refractor this
        if (piece instanceof Pawn) {
            return "";
        }
        if (piece instanceof Knight) {
            return "N";
        }
        if (piece instanceof Bishop) {
            return "B";
        }
        if (piece instanceof Rook) {
            return "R";
        }
        if (piece instanceof Queen) {
            return "Q";
        }
        if (piece instanceof King) {
            return "K";
        }
        return "?";
    }

    private String specifyFileOrRank() {
        if (piece instanceof Pawn && capturedPiece != null) {
            return Character.toString("abcdefgh".charAt(piece.getSquare().getFile()));
        }
//        if (piece instanceof Knight) {
//            // fixme: check
//        }
//        if (piece instanceof Rook) {
//
//        }
        return "";
    }
}
