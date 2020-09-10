package org.example.javachessclient.chess.models;

public class Bishop extends Piece {
    public Bishop(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    static boolean checkIsAlongDiagonal(Board board, boolean isWhite, int fromFile, int fromRank, int toFile, int toRank) {
        if (Math.abs(toFile - fromFile) == Math.abs(toRank - fromRank)) {
            // check landing piece is not same color
            Piece landingPiece = board.get(toRank).get(toFile);
            if (landingPiece != null && isWhite == landingPiece.isWhite) {
                return false;
            }

            // check there are no pieces in between
            int fileDir = (toFile - fromFile) / Math.abs(toFile - fromFile);
            int rankDir = (toRank - fromRank) / Math.abs(toRank - fromRank);
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
        return "/icons/pieces/bishop-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        // ensure is not the same file (also eliminates same square)
        if (toFile == fromFile || toRank == fromRank) return false;
        return checkIsAlongDiagonal(board, isWhite, fromFile, fromRank, toFile, toRank);
    }
}
