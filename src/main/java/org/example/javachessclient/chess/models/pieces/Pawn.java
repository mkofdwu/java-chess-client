package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Square;

public class Pawn extends Piece {
    public Pawn(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/pawn-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        int direction = isWhite ? -1 : 1;
        if (fromFile == toFile
                && toRank - fromRank == 2 * direction
                && fromRank == (isWhite ? 6 : 1)
                && board.get(fromRank + direction).get(fromFile) == null
                && board.get(fromRank + direction * 2).get(fromFile) == null) {
            // moving 2 squares forward
            return true;
        }
        if (fromFile == toFile
                && toRank - fromRank == direction
                && board.get(toRank).get(fromFile) == null) {
            // move one square forward
            return true;
        }
        if (Math.abs(toFile - fromFile) == 1 && toRank - fromRank == direction) {
            // capture
            Piece piece = board.get(toRank).get(toFile);
            return piece != null && isWhite ^ piece.isWhite;
        }
        return false;
    }
}
