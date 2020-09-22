package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.King;
import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;

public class Castling extends SpecialMove {
    @Override
    public ArrayList<Move> findMoves(Piece piece) {
        ArrayList<Move> available = new ArrayList<>();
        Chess chess = piece.getChess();
        // TODO: check for leaving king in check / blocking pieces
        if (piece instanceof King) {
            if (piece.getIsWhite()) {
                if (chess.getWhiteCanCastleKingside()) {
                    available.add(new Move(piece, new Square(6, 7), MoveType.castling));
                }
                if (chess.getWhiteCanCastleQueenside()) {
                    available.add(new Move(piece, new Square(1, 7), MoveType.castling));
                }
            } else {
                if (chess.getBlackCanCastleKingside()) {
                    available.add(new Move(piece, new Square(6, 0), MoveType.castling));
                }
                if (chess.getBlackCanCastleQueenside()) {
                    available.add(new Move(piece, new Square(1, 0), MoveType.castling));
                }
            }
        }
        return available;
    }

    @Override
    public void perform(Move move) {
        Chess chess = move.getPiece().getChess();
        // TODO
    }
}
