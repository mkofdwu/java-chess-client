package org.example.javachessclient.chess;

import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.King;
import org.example.javachessclient.chess.models.pieces.Pawn;
import org.example.javachessclient.chess.models.pieces.Piece;
import org.example.javachessclient.chess.models.pieces.Rook;

public class FlagsHandler {
    private final Chess chess;
    private boolean whiteCanCastleKingside;
    private boolean whiteCanCastleQueenside;
    private boolean blackCanCastleKingside;
    private boolean blackCanCastleQueenside;
    private Square enPassantSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    public FlagsHandler(Chess chess) {
        this.chess = chess;
    }

    public void updateFlags(Move lastMove) {
        updateCastlingAvailability(lastMove);
        updateEnPassantSquare(lastMove);
        updateThreefoldRepetition(lastMove);
        updateFiftyMoveRule(lastMove);
        fullmoveNumber++;
    }

    private void updateCastlingAvailability(Move lastMove) {
        if (lastMove.getPiece() instanceof King) {
            if (chess.getWhiteToMove()) {
                whiteCanCastleKingside = false;
                whiteCanCastleQueenside = false;
            } else {
                blackCanCastleKingside = false;
                blackCanCastleQueenside = false;
            }
        } else if (lastMove.getPiece() instanceof Rook) {
            int fromFile = lastMove.getFromSquare().getFile();
            if (chess.getWhiteToMove()) {
                if (fromFile == 0) whiteCanCastleQueenside = false;
                else if (fromFile == 7) whiteCanCastleKingside = false;
            } else {
                if (fromFile == 0) blackCanCastleQueenside = false;
                else if (fromFile == 7) blackCanCastleKingside = false;
            }
        }
    }

    private void updateEnPassantSquare(Move lastMove) {
        Piece piece = lastMove.getPiece();
        int toFile = lastMove.getToSquare().getFile();
        int toRank = lastMove.getToSquare().getRank();
        boolean movedTwoSquares = Math.abs(toRank - lastMove.getFromSquare().getRank()) == 2;
        if (piece instanceof Pawn && movedTwoSquares) {
            Piece piece1 = chess.pieceAt(toFile - 1, toRank);
            Piece piece2 = chess.pieceAt(toFile + 1, toRank);
            if ((piece1 instanceof Pawn && piece1.getIsWhite() != piece.getIsWhite())
                    || (piece2 instanceof Pawn && piece2.getIsWhite() != piece.getIsWhite())) {
                enPassantSquare = new Square(toFile, toRank + (piece.getIsWhite() ? 1 : -1));
                return;
            }
        }
        enPassantSquare = null;
    }

    private void updateThreefoldRepetition(Move lastMove) {
        // TODO
    }

    private void updateFiftyMoveRule(Move lastMove) {
        if (lastMove.getPiece() instanceof Pawn || lastMove.getCapturedPiece() != null) {
            halfmoveClock = 0;
        } else {
            ++halfmoveClock;
        }
    }

    public boolean getWhiteCanCastleKingside() {
        return whiteCanCastleKingside;
    }

    public boolean getWhiteCanCastleQueenside() {
        return whiteCanCastleQueenside;
    }

    public boolean getBlackCanCastleKingside() {
        return blackCanCastleKingside;
    }

    public boolean getBlackCanCastleQueenside() {
        return blackCanCastleQueenside;
    }

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public void setWhiteCanCastleKingside(boolean whiteCanCastleKingside) {
        this.whiteCanCastleKingside = whiteCanCastleKingside;
    }

    public void setWhiteCanCastleQueenside(boolean whiteCanCastleQueenside) {
        this.whiteCanCastleQueenside = whiteCanCastleQueenside;
    }

    public void setBlackCanCastleKingside(boolean blackCanCastleKingside) {
        this.blackCanCastleKingside = blackCanCastleKingside;
    }

    public void setBlackCanCastleQueenside(boolean blackCanCastleQueenside) {
        this.blackCanCastleQueenside = blackCanCastleQueenside;
    }

    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public void setHalfmoveClock(int halfmoveClock) {
        this.halfmoveClock = halfmoveClock;
    }

    public void setFullmoveNumber(int fullmoveNumber) {
        this.fullmoveNumber = fullmoveNumber;
    }
}
