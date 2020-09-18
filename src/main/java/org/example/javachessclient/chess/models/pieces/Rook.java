package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
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
        ArrayList<Move> available = new ArrayList<>();
        for (Square square : chess.lineSquares(square, isWhite)) {
            // TODO
            Move move = new Move(this, square, MoveType.normal);
            if (!chess.moveLeavesKingInCheck(move)) {
                available.add(move);
            }
        }
        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {

    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        if (toFile == fromFile && toRank == fromRank) return false;
        return checkIsAlongLine(board, isWhite, fromFile, fromRank, toFile, toRank);
    }
}
