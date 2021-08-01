package org.example.javachessclient.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.AuthService;
import org.example.javachessclient.services.ThemeService;
import org.example.javachessclient.views.LoadingModal;

public class LoginController {
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    @FXML
    void onGoToRegister() {
        Store.router.replace("/fxml/register.fxml");
    }

    @FXML
    void onLogin() {
        Store.modal.show(LoadingModal.buildModal());
        new Thread(() -> {
            String errorMessage = AuthService.authenticate(usernameInput.getText(), passwordInput.getText(), false);
            Platform.runLater(() -> {
                Store.modal.hide();
                if (errorMessage == null) {
                    ThemeService.setTheme(Store.user.getSettings().getTheme());
                    ThemeService.setAccent(Store.user.getSettings().getAccent());
                    Store.router.push("/fxml/layout.fxml");
                } else {
                    Store.modal.showMessage("Failed to login", errorMessage);
                }
            });
        }).start();
    }
}
