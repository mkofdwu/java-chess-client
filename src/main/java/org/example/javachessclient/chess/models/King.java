package org.example.javachessclient.chess.models;

public class King extends Piece {
    public King(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/king-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        if (toFile == fromFile && toRank == fromRank) return false;
        return Math.abs(toFile - fromFile) <= 1 && Math.abs(toRank - fromRank) <= 1;
    }
}
