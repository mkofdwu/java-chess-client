package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Board;
import org.example.javachessclient.chess.Square;
import org.example.javachessclient.chess.models.specialmoves.SpecialMove;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/knight-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<SpecialMove> getSpecialMoves() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Square> availableSquares() {
        ArrayList<Square> available = new ArrayList<>();

        int fromFile = square.getFile();
        int fromRank = square.getRank();

        for (int fileDir : new int[] {-1, 1}) {
            for (int rankDir : new int[] {-1, 1}) {
                for (int absFilesMoved : new int[] {1, 2}) {
                    int absRanksMoved = 3 - absFilesMoved;
                    int filesMoved = absFilesMoved * fileDir;
                    int ranksMoved = absRanksMoved * rankDir;
                    Square newSquare = new Square(fromFile + filesMoved, fromRank + ranksMoved);
                    Piece piece =
                    if (board.pieceAt())
                }
            }
        }

        return available;
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
