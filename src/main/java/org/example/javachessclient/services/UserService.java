package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.models.UpdateProfileDetails;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;

import java.io.IOException;

public class UserService {
    public static void updateUser(UpdateProfileDetails details) {
        try {
            Store.userApi.updateUser(details).execute();
        } catch (IOException exception) {
            System.out.println("Failed to update user: IOException: " + exception.getMessage());
        }
    }

    public static void deleteUser() {
        try {
            Store.userApi.deleteUser().execute();
        } catch (IOException exception) {
            System.out.println("Failed to delete user: IOException: " + exception.getMessage());
        }
    }

    public static void updateUserPassword(String oldPassword, String newPassword) {
        try {
            Store.userApi.updateUserPassword(oldPassword, newPassword).execute();
        } catch (IOException exception) {
            System.out.println("Failed to update user password: IOException: " + exception.getMessage());
        }
    }

    public static void updateUserGame(String gameId, UserGameUpdateDetails details) {
        try {
            Store.userApi.updateUserGame(gameId, details).execute();
        } catch (IOException exception) {
            System.out.println("Failed to update user game: IOException: " + exception.getMessage());
        }
    }

    public static UserProfile getUserProfile(String userId) {
        try {
            return Store.userApi.getUserProfile(userId).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to get user profile: IOException: " + exception.getMessage());
            return null;
        }
    }

    public static UserProfile searchByUsername(String username) {
        try {
            return Store.userApi.searchByUsername(username).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to search for user profile: IOException: " + exception.getMessage());
            return null;
        }
    }
}
