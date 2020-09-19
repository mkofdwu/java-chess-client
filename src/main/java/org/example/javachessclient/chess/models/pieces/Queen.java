package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/queen-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();
        ArrayList<Move> movesToCheck = chess.diagonalMoves(this);
        movesToCheck.addAll(chess.lineMoves(this));
        for (Move move : movesToCheck) {
            if (!chess.moveLeavesKingInCheck(move)) {
                available.add(move);
            }
        }
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        // a queen has no special moves
    }
}
