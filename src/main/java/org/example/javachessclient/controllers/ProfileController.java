package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.UpdateProfileDetails;
import org.example.javachessclient.services.AuthService;
import org.example.javachessclient.services.UserService;

import java.io.File;

public class ProfileController {
    @FXML
    private TextField usernameInput;

    @FXML
    private TextArea bioInput;

    @FXML
    private StackPane profilePicStack;

    @FXML
    private VBox addProfilePicBox;

    @FXML
    private ImageView profilePicView;

    @FXML
    private Button changeProfilePicButton;

    public void initialize() {
        profilePicStack.setClip(new Circle(90, 90, 90));

        usernameInput.setText(Store.user.getUsername());
        bioInput.setText(Store.user.getBio());
        if (Store.user.getProfilePic() == null || Store.user.getProfilePic().isEmpty()) {
            addProfilePicBox.setVisible(true);
            profilePicView.setVisible(false);
        } else {
            profilePicView.setPreserveRatio(false);  // FIXME: cover fit image
            profilePicView.setImage(new Image(Store.user.getProfilePic()));
        }
    }

    @FXML
    void onChangeUsername() {
        UpdateProfileDetails details = new UpdateProfileDetails();
        details.setUsername(usernameInput.getText());
        UserService.updateUser(details);
    }

    @FXML
    void onChangeBio() {
        UpdateProfileDetails details = new UpdateProfileDetails();
        details.setBio(bioInput.getText());
        UserService.updateUser(details);
    }

    @FXML
    void onHoverProfilePic() {
        if (profilePicView.isVisible())
            changeProfilePicButton.setVisible(true);
    }

    @FXML
    void onExitHoverProfilePic() {
        if (changeProfilePicButton.isVisible())
            changeProfilePicButton.setVisible(false);
    }

    @FXML
    void onChangeProfilePic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.gif"));
        File profilePicFile = fileChooser.showOpenDialog(usernameInput.getScene().getWindow());
        // TODO: send multipart request
    }

    @FXML
    void onRequestChangePassword() {
        Store.router.push("/fxml/change-password.fxml");
    }

    @FXML
    void onRequestLogout() {
        Store.modal.showOptions(
                "Are you sure?",
                "Log out out of your account?",
                new String[]{"Yes", "No"},
                (option) -> {
                    if (option.equals("Yes")) {
                        AuthService.logout();
                        Store.router.clearAndPush("/fxml/landing.fxml");
                    }
                }
        );
    }

    @FXML
    void onRequestDeleteAccount() {
        Store.modal.showOptions(
                "Are you sure?",
                "Deleting your account is irreversible!",
                new String[]{"Yes", "No"},
                (option) -> {
                    if (option.equals("Yes")) {
                        AuthService.logout();
                        UserService.deleteUser();
                        Store.router.clearAndPush("/fxml/landing.fxml");
                    }
                }
        );
    }
}

