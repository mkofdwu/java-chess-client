package org.example.javachessclient.chess;

import org.example.javachessclient.chess.models.pieces.Piece;

import java.util.ArrayList;

public class Board extends ArrayList<ArrayList<Piece>> { // typedef
    public Piece pieceAt(Square square) {
        return get(square.getRank()).get(square.getFile());
    }
}
