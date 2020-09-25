package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.exceptions.InvalidMoveException;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public class King extends Piece {

    public King(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/king-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();

        for (int filesMoved : new int[]{-1, 0, 1}) {
            for (int ranksMoved : new int[]{-1, 0, 1}) {
                Square newSquare = new Square(fromFile + filesMoved, fromRank + ranksMoved);
                Piece piece = chess.pieceAt(newSquare);
                if (piece == null || piece.getIsWhite() != isWhite) {
                    available.add(new Move(this, square, newSquare, piece == null ? MoveType.normal : MoveType.capture));
                }
            }
        }

        available.addAll(findCastlingMoves());

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    private ArrayList<Move> findCastlingMoves() {
        ArrayList<Move> available = new ArrayList<>();
        if (chess.activeKingInCheck()) return available; // cannot castle when in check
        if (isWhite) {
            if (chess.getWhiteCanCastleKingside() && checkIfCanCastle(true)) {
                available.add(new Move(this, square, new Square(6, 7), MoveType.castling));
            }
            if (chess.getBlackCanCastleQueenside() && checkIfCanCastle(false)) {
                available.add(new Move(this, square, new Square(2, 7), MoveType.castling));
            }
        } else {
            if (chess.getBlackCanCastleKingside() && checkIfCanCastle(true)) {
                available.add(new Move(this, square, new Square(6, 0), MoveType.castling));
            }
            if (chess.getBlackCanCastleQueenside() && checkIfCanCastle(false)) {
                available.add(new Move(this, square, new Square(2, 0), MoveType.castling));
            }
        }
        return available;
    }

    private boolean checkIfCanCastle(boolean isKingside) {
        // previous checks for game flags and if the king is in check have already been carried out above

        int rank = square.getRank(); // should be 0 or 7 depending on white or black
        int fileDir = isKingside ? 1 : -1;
        int fileLimit = isKingside ? 2 : 6;
        for (int file = 4; file <= fileLimit; file += fileDir) {
            Square checkSquare = new Square(file, rank);
            Piece piece = chess.pieceAt(checkSquare);
            if (piece == null) {
                if (chess.squareIsAttacked(checkSquare)) return false;
            } else return false;
        }

        return true;
    }

    @Override
    public void makeSpecialMove(Move move) {
        if (move.getType() == MoveType.castling) {
            int rank = square.getRank();
            if (square.getFile() == 6) {
                // kingside
                Piece rook = chess.pieceAt(7, rank);
                chess.moveTo(rook, new Square(5, rank));
            } else if (square.getFile() == 2) {
                // queenside
                Piece rook = chess.pieceAt(0, rank);
                chess.moveTo(rook, new Square(3, rank));
            } else {
                throw new InvalidMoveException();
            }
        }
    }

    @Override
    public void undoSpecialMove(Move move) {
        if (move.getType() == MoveType.castling) {
            // TODO
        }
    }
}
