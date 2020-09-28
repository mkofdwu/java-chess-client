package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.apis.AuthApi;
import org.example.javachessclient.apis.UserApi;
import org.example.javachessclient.models.TokenAuthResponse;
import org.example.javachessclient.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AuthService {
    private static final String jwtFilePath = System.getenv("APPDATA") + "/JavaChess/token.txt"; // stores the jwt unencrypted

    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8081/api/").addConverterFactory(GsonConverterFactory.create()).build();
    private static final AuthApi authApi = retrofit.create(AuthApi.class);

    public static boolean attemptAuthenticateFromFile() {
        // attempt to authenticate using the jwt stored in `jwtFilePath`
        try {
            BufferedReader br = new BufferedReader(new FileReader(jwtFilePath));
            String token = br.readLine();
            br.close();
            Response<User> response = authApi.getUser("Bearer " + token).execute();
            Store.user = response.body();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to authenticate from jwt file: " + exception.getMessage());
            return false;
        }
    }

    public static boolean authenticate(String username, String password, boolean register) {
        CompletableFuture<TokenAuthResponse> authResponseFuture = new CompletableFuture<>();
        (register ? authApi.register(username, password) : authApi.login(username, password)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TokenAuthResponse> call, Response<TokenAuthResponse> response) {
                authResponseFuture.complete(response.body());
            }

            @Override
            public void onFailure(Call<TokenAuthResponse> call, Throwable throwable) {
                authResponseFuture.completeExceptionally(throwable);
            }
        });
        try {
            TokenAuthResponse authResponse = authResponseFuture.join();
            // save token to file
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(jwtFilePath));
                bw.write(authResponse.getToken());
                bw.close();
            } catch (IOException exception) {
                System.out.println("Failed to write token to file: " + exception.getMessage());
                return false;
            }
            // update Store.user
            Store.user = authResponse.getUser();
            return true;
        } catch (CompletionException exception) {
            System.out.println("Failed to authenticate: " + exception.getMessage());
            return false;
        }
    }
}
