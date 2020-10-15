package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Pawn;

import java.util.ArrayList;
import java.util.List;

public class EnPassant implements SpecialEffect {
    private final Chess chess;
    private final Square captureSquare;

    public static List<Move> findSpecialMoves(Pawn pawn) {
        Chess chess = pawn.getChess();
        Square square = pawn.getSquare();
        ArrayList<Move> available = new ArrayList<>();
        Square enPassantSquare = chess.getEnPassantSquare();
        if (enPassantSquare != null) {
            int enPassantFile = enPassantSquare.getFile();
            int enPassantRank = enPassantSquare.getRank();
            int file = square.getFile();
            int rank = square.getRank();
            int rankDir = pawn.getIsWhite() ? -1 : 1;
            Square captureSquare = new Square(enPassantFile, enPassantRank - rankDir);
            if (rank + rankDir == enPassantRank && Math.abs(file - enPassantFile) == 1) {
                available.add(new Move(pawn, square, enPassantSquare, chess.pieceAt(captureSquare), new EnPassant(chess, captureSquare)));
            }
        }
        return available;
    }

    public EnPassant(Chess chess, Square captureSquare) {
        this.chess = chess;
        this.captureSquare = captureSquare;
    }

    @Override
    public List<Square> perform() {
        // make the second move, removing the pawn behind where the current pawn is now
        // some modifications had to be made for sockets to work (cannot use .getCapturedPiece())
        chess.removeAt(captureSquare);
        return List.of(captureSquare);
    }

    @Override
    public void undo() {
        // no additional steps required for en passant
    }
}
