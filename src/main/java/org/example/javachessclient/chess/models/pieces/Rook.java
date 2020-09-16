package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Board;
import org.example.javachessclient.chess.Square;

public class Rook extends Piece {
    public Rook(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    static boolean checkIsAlongLine(Board board, boolean isWhite, int fromFile, int fromRank, int toFile, int toRank) {
        if (toFile == fromFile || toRank == fromRank) {
            // check landing piece is not same color
            Piece landingPiece = board.get(toRank).get(toFile);
            if (landingPiece != null && isWhite == landingPiece.isWhite) {
                return false;
            }

            // check there are no pieces in between
            int fileDir = toFile == fromFile ? 0 : (toFile - fromFile) / Math.abs(toFile - fromFile);
            int rankDir = toRank == fromRank ? 0 : (toRank - fromRank) / Math.abs(toRank - fromRank);
            for (int checkFile = fromFile + fileDir, checkRank = fromRank + rankDir; Math.abs(toFile - checkFile) > 0; checkFile += fileDir, checkRank += rankDir) {
                if (board.get(checkRank).get(checkFile) != null) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/rook-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        if (toFile == fromFile && toRank == fromRank) return false;
        return checkIsAlongLine(board, isWhite, fromFile, fromRank, toFile, toRank);
    }
}
