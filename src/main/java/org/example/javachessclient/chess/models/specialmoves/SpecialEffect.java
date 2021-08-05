package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Square;

import java.util.List;

public abstract class SpecialEffect {
    protected Chess chess;

    public SpecialEffect(Chess chess) {
        this.chess = chess;
    }

    public abstract List<Square> perform();

    public abstract void undo();

    public static SpecialEffect fromString(Chess chess, String string) {
        if (string == null) return null;
        switch (string.charAt(0)) {
            case 'c':
                return Castling.fromString(chess, string.substring(1));
            case 'e':
                return EnPassant.fromString(chess, string.substring(1));
        }
        return null;
    }
}
