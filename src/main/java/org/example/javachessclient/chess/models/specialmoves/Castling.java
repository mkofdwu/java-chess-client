package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.exceptions.InvalidMoveException;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.King;
import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class Castling extends SpecialEffect {
    private final King king;
    private final boolean isKingside;

    public static List<Move> findSpecialMoves(King king) {
        Chess chess = king.getChess();
        Square square = king.getSquare();
        boolean isWhite = king.getIsWhite();

        ArrayList<Move> available = new ArrayList<>();
        if (chess.squareIsAttacked(square, !isWhite)) return available; // cannot castle when in check
        if (isWhite) {
            if (chess.getWhiteCanCastleKingside() && checkIfCanCastle(chess, true, true)) {
                available.add(new Move(king, square, new Square(6, 7), null, new Castling(king, true)));
            }
            if (chess.getWhiteCanCastleQueenside() && checkIfCanCastle(chess, true, false)) {
                available.add(new Move(king, square, new Square(2, 7), null, new Castling(king, false)));
            }
        } else {
            if (chess.getBlackCanCastleKingside() && checkIfCanCastle(chess, false, true)) {
                available.add(new Move(king, square, new Square(6, 0), null, new Castling(king, true)));
            }
            if (chess.getBlackCanCastleQueenside() && checkIfCanCastle(chess, false, false)) {
                available.add(new Move(king, square, new Square(2, 0), null, new Castling(king, false)));
            }
        }
        System.out.println("available castling moves: " + available);
        return available;
    }

    private static boolean checkIfCanCastle(Chess chess, boolean isWhite, boolean isKingside) {
        // previous checks for game flags and if the king is in check have already been carried out above
        int rank = isWhite ? 7 : 0; // should be 0 or 7 depending on white or black
        int fileDir = isKingside ? 1 : -1;
        int fileLimit = isKingside ? 6 : 2;
        for (int file = 4 + fileDir; file != fileLimit; file += fileDir) {
            Square checkSquare = new Square(file, rank);
            Piece piece = chess.pieceAt(checkSquare);
            if (piece == null) {
                if (chess.squareIsAttacked(checkSquare, !isWhite)) return false;
            } else return false;
        }

        return true;
    }

    public Castling(King king, boolean isKingside) {
        super(king.getChess());
        this.king = king;
        this.isKingside = isKingside;
    }

    public static Castling fromString(Chess chess, String string) { // TODO: make this more
        Square kingSquare = new Square(string.substring(0, 2));
        boolean isKingside = string.charAt(2) == '1';
        return new Castling((King) chess.pieceAt(kingSquare), isKingside);
    }

    @Override
    public List<Square> perform() {
        // perform second effect of this special move: move the rook
        Chess chess = king.getChess();
        Square square = king.getSquare();
        int rank = square.getRank();
        if (square.getFile() == 6) {
            // kingside
            Piece rook = chess.pieceAt(7, rank);
            chess.moveTo(rook, new Square(5, rank));
            return List.of(new Square(5, rank), new Square(7, rank));
        } else if (square.getFile() == 2) {
            // queenside
            Piece rook = chess.pieceAt(0, rank);
            chess.moveTo(rook, new Square(3, rank));
            return List.of(new Square(3, rank), new Square(0, rank));
        } else {
            throw new InvalidMoveException();
        }
    }

    @Override
    public void undo() {
        // move back the rook
        Chess chess = king.getChess();
        int rank = king.getIsWhite() ? 7 : 0;
        if (isKingside) {
            chess.moveTo(chess.pieceAt(5, rank), new Square(7, rank));
        } else {
            chess.moveTo(chess.pieceAt(3, rank), new Square(0, rank));
        }

        if (king.getIsWhite()) {
            // fixme: if the other side already cannot castle because of moving the rook this will reset it to be allowed
            chess.setWhiteCanCastleKingside(true);
            chess.setWhiteCanCastleQueenside(true);
        } else {
            chess.setBlackCanCastleKingside(true);
            chess.setBlackCanCastleQueenside(true);
        }
    }
}
