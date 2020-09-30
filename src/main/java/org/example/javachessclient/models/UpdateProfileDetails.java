package org.example.javachessclient.models;

public class UpdateProfileDetails {
    private String username;
    private String profilePic;
    private String bio;
    private UserSettings settings;

    public UpdateProfileDetails() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }
}
