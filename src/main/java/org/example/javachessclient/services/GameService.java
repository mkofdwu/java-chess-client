package org.example.javachessclient.services;

import org.example.javachessclient.apis.GameApi;
import org.example.javachessclient.models.Game;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class GameService {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081/api/game/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final GameApi gameApi = retrofit.create(GameApi.class);

    public static Game getGame(String gameId) {
        try {
            return gameApi.getGame(gameId).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to fetch game with id " + gameId + ": " + exception.getMessage());
            return null;
        }
    }
}
