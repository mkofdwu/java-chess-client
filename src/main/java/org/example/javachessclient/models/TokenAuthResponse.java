package org.example.javachessclient.models;

public class TokenAuthResponse {
    private final String token;
    private final User user;

    public TokenAuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
