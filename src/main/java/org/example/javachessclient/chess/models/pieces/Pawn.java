package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.exceptions.BadSquare;
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
        boolean isAtStartingRank = fromRank == (isWhite ? 6 : 1);

        // check for normal moves
        for (int ranks = 1; ranks <= (isAtStartingRank ? 2 : 1); ++ranks) {
            try {
                Square newSquare = new Square(fromFile, fromRank + dir * ranks);
                Piece piece = chess.pieceAt(newSquare);
                if (piece == null) available.add(new Move(this, square, newSquare, MoveType.normal, null));
                else break;
            } catch (BadSquare exception) {
                // probably almost reached the end to promote
            }
        }

        // check for capture
        for (int fileDir : new int[]{-1, 1}) {
            try {
                Square newSquare = new Square(fromFile + fileDir, fromRank + dir);
                Piece piece = chess.pieceAt(newSquare);
                if (piece != null && piece.getIsWhite() != isWhite) {
                    available.add(new Move(this, square, newSquare, MoveType.capture, piece));
                }
            } catch (BadSquare exception) {
                // probably pawns are on the sides
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
                available.add(new Move(this, square, enPassantSquare, MoveType.enPassant, chess.pieceAt(enPassantFile, enPassantRank - rankDir)));
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
    public Square[] makeSpecialMoveAndGetAffectedSquares(Move move) {
        if (move.getType() == MoveType.enPassant) {
            // make the second move, removing the pawn behind where the current pawn is now
            // some modifications had to be made for sockets to work (cannot use .getCapturedPiece())
            int toFile = move.getToSquare().getFile();
            int toRank = move.getToSquare().getRank();
            Square square = new Square(toFile, toRank - (isWhite ? -1 : 1));
            chess.removeAt(square);
            return new Square[]{square};
        }
        return null;
    }

    @Override
    public void undoSpecialMove(Move move) {
        // no additional steps required for en passant
    }
}
