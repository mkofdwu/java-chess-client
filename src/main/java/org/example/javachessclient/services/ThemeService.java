package org.example.javachessclient.services;

import org.example.javachessclient.Store;

public class ThemeService {
    private final static String[] themes = new String[]{"light-theme", "dark-theme"};
    private final static String[] accents = new String[]{"grey-accent", "blue-accent", "green-accent"};

    public static void setTheme(int themeIndex) {
        Store.root.getStyleClass().removeIf((className) -> className.endsWith("-theme"));
        Store.root.getStyleClass().add(themes[themeIndex]);
    }

    public static void setAccent(int accentIndex) {
        Store.root.getStyleClass().removeIf((className) -> className.endsWith("-accent"));
        Store.root.getStyleClass().add(accents[accentIndex]);
    }
}
