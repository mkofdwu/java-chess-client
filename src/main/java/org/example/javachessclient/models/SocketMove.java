package org.example.javachessclient.models;

public class SocketMove {
    private String gameId;
    private int fromFile;
    private int fromRank;
    private int toFile;
    private int toRank;
    private String moveType;
    private String updatedFenPosition; // temporary solution but may pose security concerns

    public SocketMove() {
    }

    public SocketMove(String gameId, int fromFile, int fromRank, int toFile, int toRank, String moveType, String updatedFenPosition) {
        this.gameId = gameId;
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.toFile = toFile;
        this.toRank = toRank;
        this.moveType = moveType;
        this.updatedFenPosition = updatedFenPosition;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getUpdatedFenPosition() {
        return updatedFenPosition;
    }

    public void setUpdatedFenPosition(String updatedFenPosition) {
        this.updatedFenPosition = updatedFenPosition;
    }
}
