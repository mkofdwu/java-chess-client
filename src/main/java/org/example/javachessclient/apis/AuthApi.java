package org.example.javachessclient.apis;

import org.example.javachessclient.models.LoginDetails;
import org.example.javachessclient.models.RegisterDetails;
import org.example.javachessclient.models.TokenAuthResponse;
import org.example.javachessclient.models.User;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {
    @GET("auth/user")
    Call<User> getUser();

    @POST("auth/login")
    Call<TokenAuthResponse> login(@Body LoginDetails loginDetails);

    @POST("auth/register")
    Call<TokenAuthResponse> register(@Body RegisterDetails registerDetails);
}
