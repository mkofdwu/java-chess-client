package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Board;
import org.example.javachessclient.chess.Square;
import org.example.javachessclient.chess.models.specialmoves.Castling;
import org.example.javachessclient.chess.models.specialmoves.SpecialMove;

import java.util.ArrayList;

public class King extends Piece {
    public King(Board board, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/king-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<SpecialMove> getSpecialMoves() {
        ArrayList<SpecialMove> specialMoves = new ArrayList<SpecialMove>();
        specialMoves.add(new Castling());
        return specialMoves;
    }

    @Override
    public ArrayList<Square> availableSquares() {
        ArrayList<Square> available = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();

        for (int filesMoved : new int[] {-1, 0, 1}) {
            for (int ranksMoved : new int[] {-1, 0, 1}) {
                Square newSquare = new Square(fromFile + filesMoved, fromRank + ranksMoved);
                Piece piece = board.pieceAt(newSquare);
                if (piece == null || piece.getIsWhite() != isWhite) {
                    available.add(newSquare);
                }
            }
        }

        // TODO: check for check

        return available;
    }
}
