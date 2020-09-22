package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/rook-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = chess.lineMoves(this);
        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        // A rook has no special moves
    }
}
