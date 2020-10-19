package org.example.javachessclient.models;

public class RecordedMove {
    private int fromFile;
    private int fromRank;
    private int toFile;
    private int toRank;
    private String specialEffectString;

    public RecordedMove() {
    }

    public int getFromFile() {
        return fromFile;
    }

    public void setFromFile(int fromFile) {
        this.fromFile = fromFile;
    }

    public int getFromRank() {
        return fromRank;
    }

    public void setFromRank(int fromRank) {
        this.fromRank = fromRank;
    }

    public int getToFile() {
        return toFile;
    }

    public void setToFile(int toFile) {
        this.toFile = toFile;
    }

    public int getToRank() {
        return toRank;
    }

    public void setToRank(int toRank) {
        this.toRank = toRank;
    }

    public String getSpecialEffectString() {
        return specialEffectString;
    }

    public void setSpecialEffectString(String specialEffectString) {
        this.specialEffectString = specialEffectString;
    }
}
