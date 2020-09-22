package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.specialmoves.EnPassant;

import java.util.ArrayList;

public class Pawn extends Piece {
    // special moves
    EnPassant enPassant = new EnPassant();

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

        // check for normal moves
        for (int rankDir = 1; rankDir < 3; ++rankDir) {
            Square newSquare = new Square(fromFile, fromRank + rankDir);
            Piece piece = chess.pieceAt(newSquare);
            if (piece == null) available.add(new Move(this, newSquare, MoveType.normal));
            else break;
        }

        // check for capture
        for (int fileDir : new int[]{-1, 1}) {
            Square newSquare = new Square(fromFile + fileDir, fromRank);
            Piece piece = chess.pieceAt(newSquare);
            if (piece != null && piece.getIsWhite() != isWhite) {
                available.add(new Move(this, newSquare, MoveType.capture));
            }
        }

        available.addAll(enPassant.findMoves(this));

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        if (move.getType() == MoveType.enPassant) {
            enPassant.perform(move);
        }
    }
}
