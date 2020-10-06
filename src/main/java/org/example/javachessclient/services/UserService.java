package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.example.javachessclient.models.UpdatePasswordDetails;
import org.example.javachessclient.models.UserGameUpdateDetails;
import org.example.javachessclient.models.UserProfile;

import java.io.IOException;

public class UserService {
    private static final UserProfile deletedUserProfile = new UserProfile("", "[Deleted user]", "", "");

    public static void updateUser() {
        try {
            Store.userApi.updateUser(Store.user).execute();
        } catch (IOException exception) {
            System.out.println("Failed to update user: IOException: " + exception.getMessage());
        }
    }

    public static boolean deleteUser() {
        try {
            Store.userApi.deleteUser().execute();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to delete user: IOException: " + exception.getMessage());
            return false;
        }
    }

    public static boolean updateUserPassword(String oldPassword, String newPassword) {
        try {
            Store.userApi.updateUserPassword(new UpdatePasswordDetails(oldPassword, newPassword)).execute();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to update user password: IOException: " + exception.getMessage());
            return false;
        }
    }

    public static boolean updateUserGame(String gameId, UserGameUpdateDetails details) {
        try {
            Store.userApi.updateUserGame(gameId, details).execute();
            return true;
        } catch (IOException exception) {
            System.out.println("Failed to update user game: IOException: " + exception.getMessage());
            return false;
        }
    }

    public static UserProfile getUserProfile(String userId) {
        try {
            return Store.userApi.getUserProfile(userId).execute().body();
        } catch (IOException exception) {
            System.out.println("Failed to get user profile: IOException: " + exception.getMessage());
            return deletedUserProfile;
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
