package org.example.javachessclient.models;

import java.util.Date;
import java.util.List;

public class Game {
    private String _id;
    private String white;
    private String black;
    private List<List<Integer>> moves; // in the format [ (file1, rank1, file2, rank2) ]
    private int result; // 1 - white wins, -1 - black wins, 0 - draw
    private Date timestamp;

    public Game() {
    }

    public String getId() {
        return _id;
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

    @Override
    public String toString() {
        return "Game{" +
                "_id='" + _id + '\'' +
                ", white='" + white + '\'' +
                ", black='" + black + '\'' +
                '}';
    }
}
