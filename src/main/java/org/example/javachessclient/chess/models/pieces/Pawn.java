package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.exceptions.InvalidMoveException;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/pawn-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        int dir = isWhite ? -1 : 1;

        // check for normal moves
        for (int ranks = 1; ranks < 3; ++ranks) {
            Square newSquare = new Square(fromFile, fromRank + dir * ranks);
            Piece piece = chess.pieceAt(newSquare);
            if (piece == null) available.add(new Move(this, square, newSquare, MoveType.normal));
            else break;
        }

        // check for capture
        for (int fileDir : new int[]{-1, 1}) {
            Square newSquare = new Square(fromFile + fileDir, fromRank + dir);
            Piece piece = chess.pieceAt(newSquare);
            if (piece != null && piece.getIsWhite() != isWhite) {
                available.add(new Move(this, square, newSquare, MoveType.capture));
            }
        }

        available.addAll(findEnPassantMoves());

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    private ArrayList<Move> findEnPassantMoves() {
        ArrayList<Move> available = new ArrayList<>();
        Square enPassantSquare = chess.getEnPassantSquare();
        if (enPassantSquare != null) {
            int enPassantFile = enPassantSquare.getFile();
            int enPassantRank = enPassantSquare.getRank();
            int file = square.getFile();
            int rank = square.getRank();
            int rankDir = isWhite ? -1 : 1;
            if (rank + rankDir == enPassantRank && Math.abs(file - enPassantFile) == 1) {
                available.add(new Move(this, square, enPassantSquare, MoveType.enPassant));
            }
        }
        return available;
    }

    @Override
    public boolean isAttackingSquare(Square otherSquare) {
        int dir = isWhite ? -1 : 1;
        return Math.abs(square.getFile() - otherSquare.getFile()) == 1 && otherSquare.getRank() - square.getRank() == dir;
    }

    @Override
    public void makeSpecialMove(Move move) {
        if (move.getType() == MoveType.enPassant) {
            // make the second move, removing the pawn behind where the current pawn is now
            Piece pieceRemoved = chess.removeAt(move.getToSquare().getFile(), move.getToSquare().getRank() + (isWhite ? 1 : -1));
            // just checking
            if (!(pieceRemoved instanceof Pawn) || pieceRemoved.getIsWhite() == isWhite) {
                throw new InvalidMoveException();
            }
        }
    }

    @Override
    public void undoSpecialMove(Move move) {
        if (move.getType() == MoveType.enPassant) {
            // TODO
        }
    }
}
