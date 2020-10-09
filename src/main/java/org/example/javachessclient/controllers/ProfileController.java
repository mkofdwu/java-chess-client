package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.AuthService;
import org.example.javachessclient.services.FileService;
import org.example.javachessclient.services.ThemeService;
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

    @FXML
    private Button lightThemeButton;

    @FXML
    private Button darkThemeButton;

    @FXML
    private Button greyAccentButton;

    @FXML
    private Button blueAccentButton;

    @FXML
    private Button greenAccentButton;

    public void initialize() {
        profilePicStack.setClip(new Circle(90, 90, 90));

        usernameInput.setText(Store.user.getUsername());
        bioInput.setText(Store.user.getBio());
        if (Store.user.getProfilePic() == null || Store.user.getProfilePic().isEmpty()) {
            addProfilePicBox.setVisible(true);
            profilePicView.setVisible(false);
        } else {
            refreshProfilePic();
        }

        usernameInput.focusedProperty().addListener((a, b, focused) -> {
            if (!focused) {
                if (!usernameInput.getText().isEmpty()) {
                    Store.user.setUsername(usernameInput.getText());
                    UserService.updateUser();
                }
            }
        });

        bioInput.focusedProperty().addListener((a, b, focused) -> {
            if (!focused) {
                if (!bioInput.getText().isEmpty()) {
                    Store.user.setBio(bioInput.getText());
                    UserService.updateUser();
                }
            }
        });

        Button[] themeBtns = new Button[]{lightThemeButton, darkThemeButton};
        for (int i = 0; i < 2; ++i) {
            int finalI = i;
            ;
            themeBtns[i].setOnMouseClicked((e) -> {
                ThemeService.setTheme(finalI);
                Store.user.getSettings().setTheme(finalI);
                UserService.updateUser();
            });
        }

        Button[] accentBtns = new Button[]{greyAccentButton, blueAccentButton, greenAccentButton};
        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            accentBtns[i].setOnMouseClicked((e) -> {
                ThemeService.setAccent(finalI);
                Store.user.getSettings().setAccent(finalI);
                UserService.updateUser();
            });
        }
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
        if (profilePicFile != null) {
            String profilePic = FileService.uploadImage(profilePicFile).getUrl();
            Store.user.setProfilePic(profilePic);
            refreshProfilePic();
            UserService.updateUser();
        }
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
                        UserService.deleteUser();
                        AuthService.logout();
                        Store.router.clearAndPush("/fxml/landing.fxml");
                    }
                }
        );
    }

    private void refreshProfilePic() {
        // why doesn't java have a better way of setting image fit to cover?
        Image profilePicImage = new Image(Store.user.getProfilePic());
        PixelReader reader = profilePicImage.getPixelReader();
        int imageSize = (int) Math.min(profilePicImage.getWidth(), profilePicImage.getHeight());
        int x = (int) (profilePicImage.getWidth() - imageSize) / 2;
        int y = (int) (profilePicImage.getHeight() - imageSize) / 2;
        WritableImage croppedProfilePic = new WritableImage(reader, x, y, imageSize, imageSize);
        profilePicView.setImage(croppedProfilePic);
    }
}
