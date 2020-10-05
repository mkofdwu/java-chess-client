package org.example.javachessclient.models;

import java.util.Date;
import java.util.List;

public class PastGame {
    private String _id;
    private String white;
    private String black;
    private List<List<Integer>> moves; // in the format [ (file1, rank1, file2, rank2) ]
    private int result; // 0 - draw, 1 - white wins, 2 - black wins
    private Date timestamp;

    public PastGame() {
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
