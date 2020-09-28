package org.example.javachessclient.models;

import org.example.javachessclient.models.User;

public class TokenAuthResponse {
    private String token;
    private User user;

    public TokenAuthResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
