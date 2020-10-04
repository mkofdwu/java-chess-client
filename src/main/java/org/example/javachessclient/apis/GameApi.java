package org.example.javachessclient.apis;

import org.example.javachessclient.models.OngoingGame;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GameApi {
    @GET("game/{gameId}")
    Call<OngoingGame> getGame(@Path("gameId") String gameId);

    @POST("game/random")
    Call<Void> randomGame();

    @POST("game/random/cancel")
    Call<Void> cancelSearchForRandomGame();

    @POST("game/request")
    Call<Void> requestGame(@Query("otherUserId") String otherUserId);

    @POST("game/request-response")
    Call<Void> respondToGameRequest(@Query("otherUserId") String otherUserId, @Query("accept") boolean accept);
}
