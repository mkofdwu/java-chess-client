package org.example.javachessclient.models;

import java.util.List;

public class User {
    private String _id;
    private String username;
    private String password;
    private String profilePic;
    private String bio;
    private List<UserGame> pastGames;
    private List<UserGame> ongoingGames;
    private List<String> requestsSent;
    private List<String> requestsReceived;
    private UserSettings settings;

    public User() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<UserGame> getPastGames() {
        return pastGames;
    }

    public void setPastGames(List<UserGame> pastGames) {
        this.pastGames = pastGames;
    }

    public List<UserGame> getOngoingGames() {
        return ongoingGames;
    }

    public void setOngoingGames(List<UserGame> ongoingGames) {
        this.ongoingGames = ongoingGames;
    }

    public List<String> getRequestsSent() {
        return requestsSent;
    }

    public void setRequestsSent(List<String> requestsSent) {
        this.requestsSent = requestsSent;
    }

    public List<String> getRequestsReceived() {
        return requestsReceived;
    }

    public void setRequestsReceived(List<String> requestsReceived) {
        this.requestsReceived = requestsReceived;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", bio='" + bio + '\'' +
                ", pastGames=" + pastGames +
                ", ongoingGames=" + ongoingGames +
                ", requestsSent=" + requestsSent +
                ", requestsReceived=" + requestsReceived +
                ", settings=" + settings +
                '}';
    }
}
