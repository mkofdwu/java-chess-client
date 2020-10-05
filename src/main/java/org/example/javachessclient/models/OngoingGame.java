package org.example.javachessclient.models;

import java.util.Date;
import java.util.List;

public class OngoingGame {
    private String _id;
    private String white;
    private String black;
    private List<List<Integer>> moves; // in the format [ (file1, rank1, file2, rank2) ]
    private String fenPosition; // includes game flags
    private Date timestamp;

    public OngoingGame() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public List<List<Integer>> getMoves() {
        return moves;
    }

    public void setMoves(List<List<Integer>> moves) {
        this.moves = moves;
    }

    public String getFenPosition() {
        return fenPosition;
    }

    public void setFenPosition(String fenPosition) {
        this.fenPosition = fenPosition;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
