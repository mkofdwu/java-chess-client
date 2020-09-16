package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Board;
import org.example.javachessclient.chess.Square;
import org.example.javachessclient.chess.models.specialmoves.SpecialMove;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    public static ArrayList<Square> diagonalSquares(Board board, Square square, boolean isWhite) {
        ArrayList<Square> available = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();
        for (int fileDir : new int[]{-1, 1}) {
            for (int rankDir : new int[]{-1, 1}) {
                for (
                        int file = fromFile + fileDir, rank = fromRank + rankDir;
                        0 <= file && file < 8 && 0 <= rank && rank < 8;
                        file += fileDir, rank += rankDir
                ) {
                    available.add(new Square(file, rank));
                }
            }
        }
        return available;
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/bishop-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<SpecialMove> getSpecialMoves() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Square> availableSquares() {
        return diagonalSquares(board, square, isWhite);
    }
}
