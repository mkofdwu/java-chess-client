package org.example.javachessclient.chess.models.pieces;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.exceptions.BadSquare;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Chess chess, Square square, boolean isWhite) {
        super(chess, square, isWhite);
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
                    try {
                        Square newSquare = new Square(fromFile + filesMoved, fromRank + ranksMoved);
                        Piece piece = chess.pieceAt(newSquare);
                        if (piece == null) {
                            available.add(new Move(this, square, newSquare, null, null));
                        } else if (piece.getIsWhite() != isWhite) {
                            available.add(new Move(this, square, newSquare, piece, null));
                        }
                    } catch (BadSquare exception) {
                        // do nothing
                    }
                }
            }
        }

        available.removeIf(move -> chess.moveLeavesKingInCheck(move));
        return available;
    }

    @Override
    public boolean isAttackingSquare(Square otherSquare) {
        int absFilesMoved = Math.abs(otherSquare.getFile() - square.getFile());
        int absRanksMoved = Math.abs(otherSquare.getRank() - square.getRank());
        return (absFilesMoved == 2 && absRanksMoved == 1) || (absFilesMoved == 1 && absRanksMoved == 2);
    }
}
