package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.exceptions.BadSquare;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.specialmoves.EnPassant;

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
                if (piece == null) available.add(new Move(this, square, newSquare, null, null));
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
                    available.add(new Move(this, square, newSquare, piece, null));
                }
            } catch (BadSquare exception) {
                // probably pawns are on the sides
            }
        }

        available.addAll(EnPassant.findSpecialMoves(this));

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public boolean isAttackingSquare(Square otherSquare) {
        int dir = isWhite ? -1 : 1;
        return Math.abs(square.getFile() - otherSquare.getFile()) == 1 && otherSquare.getRank() - square.getRank() == dir;
    }
}
