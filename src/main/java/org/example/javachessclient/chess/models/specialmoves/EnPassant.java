package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Pawn;
import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;

public class EnPassant extends SpecialMove {
    @Override
    public ArrayList<Move> findMoves(Piece piece) {
        ArrayList<Move> available = new ArrayList<>();
        Chess chess = piece.getChess();
        if (piece instanceof Pawn) {
            Square enPassantSquare = chess.getEnPassantSquare();
            if (enPassantSquare != null) {
                int enPassantFile = enPassantSquare.getFile();
                int enPassantRank = enPassantSquare.getRank();
                int file = piece.getSquare().getFile();
                int rank = piece.getSquare().getRank();
                int rankDir = piece.getIsWhite() ? -1 : 1; // fixme ?
                if (rank + rankDir == enPassantRank && Math.abs(file - enPassantFile) == 1) {
                    available.add(new Move(piece, enPassantSquare, MoveType.enPassant));
                }
            }
        }
        return available;
    }

    @Override
    public void perform(Move move) {
        // TODO
    }
}
