package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.AuthService;

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
        boolean success = AuthService.authenticate(usernameInput.getText(), passwordInput.getText(), false);
        if (success) {
            Store.router.push("/fxml/layout.fxml");
        } else {
            Store.modal.showMessage("Failed to login", "Please check your username and password again.");
        }
    }

    @FXML
    void onLoginWithGoogle() {
        // TODO
    }
}
