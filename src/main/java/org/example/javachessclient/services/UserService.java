package org.example.javachessclient.services;

import org.example.javachessclient.apis.UserApi;
import org.example.javachessclient.models.UpdateProfileDetails;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.io.IOException;

public class UserService {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081/api/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final UserApi userApi = retrofit.create(UserApi.class);

    public static void updateUser(UpdateProfileDetails details) {
        try {
            userApi.updateUser(details).execute();
        } catch (IOException exception) {
            System.out.println("Failed to update user: IOException: " + exception.getMessage());
        }
    }

    public static void deleteUser() {
    }

    public static void updateUserPassword(String oldPassword, String newPassword) {
    }

    public static void updateUserGame(@Path("gameId") String gameId, @Body UserGameUpdateDetails details) {
    }

    public static UserProfile getUserProfile(@Path("userId") String userId) {
        try {
            return userApi.getUserProfile(userId).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to get user profile: IOException: " + exception.getMessage());
            return null;
        }
    }

    public static void requestGame(String userId) {
        try {
            userApi.requestGame(userId).execute();
        } catch (IOException exception) {
            System.out.println("Failed with ioexception: " + exception.getMessage());
        }
    }

    public static void respondToGameRequest(String userId, boolean accept) {
        try {
            userApi.respondToGameRequest(userId, accept).execute();
        } catch (IOException exception) {
            System.out.println("Failed with ioexception: " + exception.getMessage());
        }
    }
}
