package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.specialmoves.SpecialMove;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Chess chess, Square square, boolean isWhite) {
        super(board, square, isWhite);
    }

    @Override
    public String getIconFilePath() {
        return "/icons/pieces/knight-" + (isWhite ? "white" : "black") + ".png";
    }

    @Override
    public ArrayList<Move> findAvailableMoves() {
        ArrayList<Move> available = new ArrayList<>();

        int fromFile = square.getFile();
        int fromRank = square.getRank();

        for (int fileDir : new int[]{-1, 1}) {
            for (int rankDir : new int[]{-1, 1}) {
                for (int absFilesMoved : new int[]{1, 2}) {
                    int absRanksMoved = 3 - absFilesMoved;
                    int filesMoved = absFilesMoved * fileDir;
                    int ranksMoved = absRanksMoved * rankDir;
                    Square newSquare = new Square(fromFile + filesMoved, fromRank + ranksMoved);
                    Piece piece = chess.pieceAt(newSquare);
                    if (piece == null) {
                        available.add(new Move(this, newSquare, MoveType.normal));
                    } else if (piece.getIsWhite() != isWhite) {
                        available.add(new Move(this, newSquare, MoveType.capture));
                    }
                }
            }
        }

        return available;
    }

    @Override
    public void makeSpecialMove(Move move) {
        // a knight has no special moves
    }
}
