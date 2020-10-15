package org.example.javachessclient.chess.models.specialmoves;

import org.example.javachessclient.chess.models.Square;

import java.util.List;

public interface SpecialEffect {
    List<Square> perform();
    void undo();
}
