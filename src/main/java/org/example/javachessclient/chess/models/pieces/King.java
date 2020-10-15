package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.exceptions.BadSquare;
import org.example.javachessclient.chess.exceptions.InvalidBoardException;
import org.example.javachessclient.chess.exceptions.InvalidMoveException;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.specialmoves.Castling;

import java.util.ArrayList;

public class King extends Piece {

    public King(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/king-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();
        int fromFile = square.getFile();
        int fromRank = square.getRank();

        for (int filesMoved : new int[]{-1, 0, 1}) {
            for (int ranksMoved : new int[]{-1, 0, 1}) {
                try {
                    Square newSquare = new Square(fromFile + filesMoved, fromRank + ranksMoved);
                    Piece piece = chess.pieceAt(newSquare);
                    if (piece == null || piece.getIsWhite() != isWhite) {
                        available.add(new Move(this, square, newSquare, piece, null));
                    }
                } catch (BadSquare exception) {
                    // king is probably on the edge
                }
            }
        }

        available.addAll(Castling.findSpecialMoves(this));

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public boolean isAttackingSquare(Square otherSquare) {
        return Math.abs(square.getFile() - otherSquare.getFile()) == 1
                && Math.abs(square.getRank() - otherSquare.getRank()) == 1;
    }
}
