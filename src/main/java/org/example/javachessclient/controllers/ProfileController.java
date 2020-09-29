package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.example.javachessclient.Store;

public class ProfileController {
    @FXML
    private TextField usernameInput;

    @FXML
    private TextArea bioInput;

    @FXML
    private ImageView profilePicView;

    public void initialize() {
        profilePicView.setClip(new Circle(90, 90, 90));

        usernameInput.setText(Store.user.getUsername());
        bioInput.setText(Store.user.getBio());
        if (Store.user.getProfilePic() == null || Store.user.getProfilePic().isEmpty()) {
            // TODO: show add profile pic button
        } else {
            profilePicView.setPreserveRatio(false);  // TODO: cover fit image
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

