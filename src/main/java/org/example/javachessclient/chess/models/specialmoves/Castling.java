package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.AvailableMove;
import org.example.javachessclient.chess.Board;
import org.example.javachessclient.chess.models.pieces.King;
import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;

public class Castling extends SpecialMove {
    @Override
    public ArrayList<AvailableMove> checkForMoves(Piece piece, Board board) {
        if (piece instanceof King) {
            if (piece.getIsWhite()) {
                if (toSquare.getRank() != 7) return null;
                if (toSquare.getFile() == 6 && chess.getWhiteCanCastleKingside()) {
                    chess.setWhiteCanCastleKingside(false);
                    chess.setWhiteCanCastleQueenside(false);
                    return SpecialMovesUtil.MoveType.WHITE_CASTLING_KINGSIDE;
                }
                if (toSquare.getFile() == 2 && chess.getWhiteCanCastleQueenside()) {
                    chess.setWhiteCanCastleKingside(false);
                    chess.setWhiteCanCastleQueenside(false);
                    return SpecialMovesUtil.MoveType.WHITE_CASTLING_QUEENSIDE;
                }
                return null;
            } else {
                if (toSquare.getRank() != 0) return null;
                if (toSquare.getFile() == 6 && chess.getBlackCanCastleKingside()) {
                    chess.setBlackCanCastleKingside(false);
                    chess.setBlackCanCastleQueenside(false);
                    return SpecialMovesUtil.MoveType.BLACK_CASTLING_KINGSIDE;
                }
                if (toSquare.getFile() == 2 && chess.getBlackCanCastleQueenside()) {
                    chess.setBlackCanCastleKingside(false);
                    chess.setBlackCanCastleQueenside(false);
                    return SpecialMovesUtil.MoveType.BLACK_CASTLING_QUEENSIDE;
                }
            }
        }
        return null;
    }

    @Override
    public void perform(AvailableMove move) {

    }
}
