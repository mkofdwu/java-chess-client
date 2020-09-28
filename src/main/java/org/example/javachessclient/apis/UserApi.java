package org.example.javachessclient.apis;

import org.example.javachessclient.models.User;
import org.example.javachessclient.models.UserProfile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserApi {
    @GET("/")
    public Call<User> getUser();

    @GET("/{userId}/profile")
    public Call<UserProfile> getUserProfile();

    @PUT("/")
    public Call<Void> updateUser();
}
