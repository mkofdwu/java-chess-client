package org.example.javachessclient.apis;

import org.example.javachessclient.models.Game;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GameApi {
    @GET("{gameId}")
    Call<Game> getGame(@Path("gameId") String gameId);
}
