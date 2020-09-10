package org.example.javachessclient.chess.models;

public class Knight extends Piece {
    public Knight(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/knight-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        int absFilesMoved = Math.abs(toFile - fromFile);
        int absRanksMoved = Math.abs(toRank - fromRank);
        if ((absFilesMoved == 2 && absRanksMoved == 1) || (absFilesMoved == 1 && absRanksMoved == 2)) {
            Piece landingPiece = board.get(toRank).get(toFile);
            return landingPiece == null || (isWhite ^ landingPiece.isWhite);
        }
        return false;
    }
}
