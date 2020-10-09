package org.example.javachessclient.models;

public class UserGameUpdateDetails {
    private String name;

    public UserGameUpdateDetails(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
