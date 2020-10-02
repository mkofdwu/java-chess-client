package org.example.javachessclient.models;

public class UserGame {
    private String gameId;
    private String name;
    private Boolean isWhite; // which side the user was playing on, 0 - black, 1 - white

    public UserGame() {
    }

    public UserGame(String gameId, String name, Boolean isWhite) {
        this.gameId = gameId;
        this.name = name;
        this.isWhite = isWhite;
    }

    public String getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsWhite() {
        return isWhite;
    }

    public void setIsWhite(Boolean isWhite) {
        this.isWhite = isWhite;
    }
}
