package org.example.javachessclient.apis;

import org.example.javachessclient.models.LoginDetails;
import org.example.javachessclient.models.RegisterDetails;
import org.example.javachessclient.models.TokenAuthResponse;
import org.example.javachessclient.models.User;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {
    @GET("user")
    Call<User> getUser(@Header("Authorization") String authorization);

    @POST("login")
    Call<TokenAuthResponse> login(@Body LoginDetails loginDetails);

    @POST("register")
    Call<TokenAuthResponse> register(@Body RegisterDetails registerDetails);
}
