package org.example.javachessclient.models;

public class GameRequest {
    private String otherUserId;

    public GameRequest() {
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }
}
