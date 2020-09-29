package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.apis.UserApi;
import org.example.javachessclient.models.UpdateProfileDetails;
import org.example.javachessclient.models.User;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserService {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081/api/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final UserApi userApi = retrofit.create(UserApi.class);

    public void updateUser(UpdateProfileDetails details) {
    }

    public void deleteUser() {
    }

    public void updateUserPassword(String oldPassword, String newPassword) {
    }

    public void updateUserGame(@Path("gameId") String gameId, @Body UserGameUpdateDetails details) {
    }

    public UserProfile getUserProfile(@Path("userId") String userId) {
        try {
            return userApi.getUserProfile(userId).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to get user profile: IOException: " + exception.getMessage());
            return null;
        }
    }

    public void requestGame(String userId) {
        try {
            userApi.requestGame(userId).execute();
        } catch (IOException exception) {
            System.out.println("Failed with ioexception: " + exception.getMessage());
        }
    }

    public void respondToGameRequest(String userId, boolean accept) {
        try {
            userApi.respondToGameRequest(userId, accept).execute();
        } catch (IOException exception) {
            System.out.println("Failed with ioexception: " + exception.getMessage());
        }
    }
}
