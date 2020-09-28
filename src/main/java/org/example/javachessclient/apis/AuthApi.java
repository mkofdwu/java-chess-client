package org.example.javachessclient.apis;

import org.example.javachessclient.models.TokenAuthResponse;
import org.example.javachessclient.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @GET("/user")
    Call<User> getUser(@Header("Authorization") String authorization);

    @POST("/login")
    Call<TokenAuthResponse> login(@Field("username") String username, @Field("password") String password);

    @POST("/register")
    Call<TokenAuthResponse> register(@Field("username") String username, @Field("password") String password);
}
