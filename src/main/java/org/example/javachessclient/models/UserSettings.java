package org.example.javachessclient.models;

public class UserSettings {
    private int theme;
    private int accent;

    public UserSettings() {
    }

    public UserSettings(int theme, int accent) {
        this.theme = theme;
        this.accent = accent;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getAccent() {
        return accent;
    }

    public void setAccent(int accent) {
        this.accent = accent;
    }
}
