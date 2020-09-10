package org.example.javachessclient.chess.models;

public class Queen extends Piece {
    public Queen(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/queen-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public boolean canMoveTo(int toFile, int toRank) {
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        if (toFile == fromFile && toRank == fromRank) return false;
        return Bishop.checkIsAlongDiagonal(board, isWhite, fromFile, fromRank, toFile, toRank)
                || Rook.checkIsAlongLine(board, isWhite, fromFile, fromRank, toFile, toRank);
    }
}
