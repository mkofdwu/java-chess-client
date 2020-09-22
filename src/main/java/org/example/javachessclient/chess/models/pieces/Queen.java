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
        ArrayList<Move> available = chess.diagonalMoves(this);
        available.addAll(chess.lineMoves(this));
        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        // a queen has no special moves
    }
}
