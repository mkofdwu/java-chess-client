package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.specialmoves.Castling;

import java.util.ArrayList;

public class King extends Piece {
    // special moves
    Castling castling = new Castling();

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
                    available.add(new Move(this, newSquare, piece == null ? MoveType.normal : MoveType.capture));
                }
            }
        }

        available.addAll(castling.findMoves(this));

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        if (move.getType() == MoveType.castling) {
            castling.perform(move);
        }
    }
}
