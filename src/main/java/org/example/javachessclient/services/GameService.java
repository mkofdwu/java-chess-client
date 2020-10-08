package org.example.javachessclient.services;

import com.google.gson.Gson;
import org.example.javachessclient.Store;

import java.io.IOException;

public class GameService {
    public static <T> T getGame(String gameId, Class<T> classOfT) {
        try {
            Object linkedTreeMap = Store.gameApi.getGame(gameId).execute().body();
            Gson gson = new Gson();
            return gson.fromJson(gson.toJsonTree(linkedTreeMap), classOfT);
        } catch (IOException exception) {
            System.out.println("Failed to fetch game with id " + gameId + ": " + exception.getMessage());
            return null;
        }
    }

    public static void randomGame() {
        try {
            Store.gameApi.randomGame().execute();
        } catch (IOException exception) {
            System.out.println("Failed to request random game: " + exception.getMessage());
        }
    }

    public static void cancelSearchForRandomGame() {
        try {
            Store.gameApi.cancelSearchForRandomGame().execute();
        } catch (IOException exception) {
            System.out.println("Failed to request cancel search for random game: " + exception.getMessage());
        }
    }

    public static void requestGame(String otherUserId) {
        try {
            Store.gameApi.requestGame(otherUserId).execute();
        } catch (IOException exception) {
            System.out.println("Failed to request game from user " + otherUserId + ": " + exception.getMessage());
        }
    }

    public static void respondToGameRequest(String otherUserId, boolean accept) {
        try {
            Store.gameApi.respondToGameRequest(otherUserId, accept).execute();
        } catch (IOException exception) {
            System.out.println("Failed to respond to game request from " + otherUserId + ": " + exception.getMessage());
        }
    }
}
