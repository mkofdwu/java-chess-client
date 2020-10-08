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
    public boolean isAttackingSquare(Square otherSquare) {
        boolean alongLine = square.getFile() == otherSquare.getFile() || square.getRank() == otherSquare.getRank();
        if (!alongLine) return false;
        return chess.squaresClearUntil(square.getFile(), square.getRank(), otherSquare.getFile(), otherSquare.getRank());
    }

    @Override
    public Square[] makeSpecialMoveAndGetAffectedSquares(Move move) {
        // A rook has no special moves
        return null;
    }

    @Override
    public void undoSpecialMove(Move move) {

    }
}
