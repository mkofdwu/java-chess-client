package org.example.javachessclient.socketgame.models;

public class SocketMessage {
    private String gameId;
    private String userId;
    private String text;

    public SocketMessage() {
    }

    public SocketMessage(String gameId, String userId, String text) {
        this.gameId = gameId;
        this.userId = userId;
        this.text = text;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
