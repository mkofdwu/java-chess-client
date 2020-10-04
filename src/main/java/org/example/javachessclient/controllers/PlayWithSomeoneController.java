package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;

public class PlayWithSomeoneController {
    @FXML
    private TextField usernameInput;

    @FXML
    void onSendRequest() {
        UserProfile otherProfile = UserService.searchByUsername(usernameInput.getText());
        if (otherProfile == null) {
            Store.modal.showMessage("User not found", "Sorry, we couldn't find a user named " + usernameInput.getText() + ". Try entering something else.");
        } else {
            GameService.requestGame(otherProfile.getId());
            Store.router.pop();
            Store.modal.showMessage("Sent request", "Successfully sent a game request to " + usernameInput.getText());
        }
    }
}
