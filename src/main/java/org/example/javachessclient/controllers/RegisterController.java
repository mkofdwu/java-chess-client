package org.example.javachessclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.AuthService;
import org.example.javachessclient.services.ThemeService;
import org.example.javachessclient.views.LoadingModal;

public class RegisterController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private PasswordField confirmPasswordInput;

    @FXML
    public void onGoToLogin() {
        Store.router.replace("/fxml/login.fxml");
    }

    @FXML
    public void onRegister() {
        if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
            Store.modal.show(LoadingModal.buildModal());
            new Thread(() -> {
                boolean success = AuthService.authenticate(usernameInput.getText(), passwordInput.getText(), true);
                Platform.runLater(() -> {
                    Store.modal.hide();
                    if (success) {
                        ThemeService.setTheme(Store.user.getSettings().getTheme());
                        ThemeService.setAccent(Store.user.getSettings().getAccent());
                        Store.router.push("/fxml/layout.fxml");
                    } else {
                        Store.modal.showMessage("Failed to register", "Please check your username and password again.");
                    }
                });
            }).start();
        } else {
            Store.modal.showMessage("Failed to register", "The passwords entered don't match.");
        }
    }
}
