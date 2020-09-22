package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;

public abstract class SpecialMove {
    public abstract ArrayList<Move> findMoves(Piece piece);
    public abstract void perform(Move move);
}
