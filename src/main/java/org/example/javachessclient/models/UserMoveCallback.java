package org.example.javachessclient.models;

import org.example.javachessclient.chess.models.Move;

public interface UserMoveCallback {
    void callback(Move move);
}
