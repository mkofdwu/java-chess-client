package org.example.javachessclient.apis;

import org.example.javachessclient.models.UpdatePasswordDetails;
import org.example.javachessclient.models.User;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserApi {
    @PUT("user")
    Call<Void> updateUser(@Body User updatedUser);

    @DELETE("user")
    Call<Void> deleteUser();

    @PUT("user/password")
    Call<Void> updateUserPassword(@Body UpdatePasswordDetails details);

    @PUT("user/game/{gameId}")
    Call<Void> updateUserGame(@Path("gameId") String gameId, @Body UserGameUpdateDetails details);

    @GET("user/{userId}/profile")
    Call<UserProfile> getUserProfile(@Path("userId") String userId);

    @GET("user/search")
    Call<UserProfile> searchByUsername(@Query("username") String username);
}
