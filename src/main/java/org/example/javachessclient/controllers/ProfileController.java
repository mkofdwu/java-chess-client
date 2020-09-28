package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.javachessclient.Store;

public class ProfileController {
    @FXML
    private Label usernameLabel;

    @FXML
    private Label bioLabel;

    @FXML
    private ImageView profilePicView;

    public void initialize() {
        profilePicView.setClip(new Circle(90));

        usernameLabel.setText(Store.user.getUsername());
        String bio = Store.user.getBio();
        if (bio.isEmpty()) {
            // TODO: show label btn to add bio
        } else {
            bioLabel.setText(Store.user.getBio());
        }
        if (Store.user.getProfilePic() == null || Store.user.getProfilePic().isEmpty()) {
            // TODO: show add profile pic button
        } else {
            profilePicView.setImage(new Image(Store.user.getProfilePic()));
        }
    }

    @FXML
    void onChangeUsername() {

    }
    
    @FXML
    void onChangeBio() {

    }

    @FXML
    void onHoverProfilePic() {

    }

    @FXML
    void onExitHoverProfilePic() {

    }

    @FXML
    void onChangeProfilePic() {

    }

    @FXML
    void onRequestChangePassword() {

    }
}

