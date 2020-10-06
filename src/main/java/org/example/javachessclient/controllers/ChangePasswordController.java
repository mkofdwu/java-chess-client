package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.UserService;

public class ChangePasswordController {
    @FXML
    private TextField oldPasswordInput;

    @FXML
    private TextField newPasswordInput;

    @FXML
    private TextField confirmNewPasswordInput;

    @FXML
    void onConfirm() {
        String oldPassword = oldPasswordInput.getText();
        String newPassword = newPasswordInput.getText();
        String confirmNewPassword = confirmNewPasswordInput.getText();

        if (newPassword.equals(confirmNewPassword)) {
            if (UserService.updateUserPassword(oldPassword, newPassword)) {
                Store.router.pop();
                Store.modal.showMessage("Password updated", "Your password has been successfully changed");
            } else {
                System.out.println("fail");
            }
        } else {
            Store.modal.showMessage("Cannot change password", "The new passwords entered don't match.");
        }
    }
}
