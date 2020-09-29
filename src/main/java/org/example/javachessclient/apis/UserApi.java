package org.example.javachessclient.apis;

import org.example.javachessclient.models.UpdateProfileDetails;
import org.example.javachessclient.models.User;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.concurrent.Executor;

public interface UserApi {
    @PUT
    Call<Void> updateUser(@Body UpdateProfileDetails details);

    @DELETE
    Call<Void> deleteUser();

    @FormUrlEncoded // TODO: test if this works, sending content as application/json
    @PUT("password")
    Call<Void> updateUserPassword(@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @PUT("game/{gameId}")
    Call<Void> updateUserGame(@Path("gameId") String gameId, @Body UserGameUpdateDetails details);

    @GET("{userId}/profile")
    Call<UserProfile> getUserProfile(@Path("userId") String userId);

    @POST("{userId}/request")
    Call<Void> requestGame(@Path("userId") String userId);

    @FormUrlEncoded
    @POST("{userId}/request-response")
    Call<Void> respondToGameRequest(@Path("userId") String userId, @Field("accept") boolean accept);
}
