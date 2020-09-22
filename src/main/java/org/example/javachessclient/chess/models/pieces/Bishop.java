package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/bishop-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = chess.diagonalMoves(this);
        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        // a bishop has no special moves
    }
}
