package org.example.javachessclient.apis;

import org.example.javachessclient.models.UpdateProfileDetails;
import org.example.javachessclient.models.User;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.concurrent.Executor;

public interface UserApi {
    @PUT("user")
    Call<Void> updateUser(@Body UpdateProfileDetails details);

    @DELETE("user")
    Call<Void> deleteUser();

    @FormUrlEncoded // TODO: test if this works, sending content as application/json
    @PUT("user/password")
    Call<Void> updateUserPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @PUT("user/game/{gameId}")
    Call<Void> updateUserGame(@Path("gameId") String gameId, @Body UserGameUpdateDetails details);

    @GET("user/{userId}/profile")
    Call<UserProfile> getUserProfile(@Path("userId") String userId);

    @GET("user/search")
    Call<UserProfile> searchByUsername(@Query("username") String username);
}
