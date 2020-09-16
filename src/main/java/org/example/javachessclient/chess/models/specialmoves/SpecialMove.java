package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.AvailableMove;

import java.util.ArrayList;

public abstract class SpecialMove {
    public abstract ArrayList<AvailableMove> checkForMoves();
    public abstract void perform(AvailableMove move);
}
