package org.example.javachessclient.models;

public class UserGame {
    private String gameId;
    private String name;
    private Boolean side; // which side the user was playing on, 0 - black, 1 - white

    public UserGame() {
    }

    public UserGame(String gameId, String name, Boolean side) {
        this.gameId = gameId;
        this.name = name;
        this.side = side;
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

    public Boolean getSide() {
        return side;
    }

    public void setSide(Boolean side) {
        this.side = side;
    }
}
