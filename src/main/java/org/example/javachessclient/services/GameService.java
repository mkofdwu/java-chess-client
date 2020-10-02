package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.apis.GameApi;

import java.io.IOException;

public class GameService {
    private static final GameApi gameApi = Store.retrofit.create(GameApi.class);

    public static Object getGame(String gameId) {
        try {
            return gameApi.getGame(gameId).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to fetch game with id " + gameId + ": " + exception.getMessage());
            return null;
        }
    }

    public static void randomGame() {
        try {
            gameApi.randomGame().execute();
        } catch (IOException exception) {
            System.out.println("Failed to request random game: " + exception.getMessage());
        }
    }

    public static void requestGame(String otherUserId) {
        try {
            gameApi.requestGame(otherUserId).execute();
        } catch (IOException exception) {
            System.out.println("Failed to request game from user " + otherUserId + ": " + exception.getMessage());
        }
    }

    public static void respondToGameRequest(String otherUserId, boolean accept) {
        try {
            gameApi.respondToGameRequest(otherUserId, accept).execute();
        } catch (IOException exception) {
            System.out.println("Failed to respond to game request from " + otherUserId + ": " + exception.getMessage());
        }
    }
}
