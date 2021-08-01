package org.example.javachessclient.services;

import org.example.javachessclient.Store;

public class ThemeService {
    private final static String[] themes = new String[]{"light-theme", "dark-theme"};
    private final static String[] accents = new String[]{"grey-accent", "blue-accent", "green-accent"};

    private static int selectedTheme;
    private static int selectedAccent;

    public static int getSelectedTheme() {
        return selectedTheme;
    }

    public static int getSelectedAccent() {
        return selectedAccent;
    }

    public static void setTheme(int themeIndex) {
        Store.root.getStyleClass().remove(themes[selectedTheme]);
        Store.root.getStyleClass().add(themes[themeIndex]);
        selectedTheme = themeIndex;
    }

    public static void setAccent(int accentIndex) {
        Store.root.getStyleClass().remove(accents[selectedAccent]);
        Store.root.getStyleClass().add(accents[accentIndex]);
        selectedAccent = accentIndex;
    }
}
