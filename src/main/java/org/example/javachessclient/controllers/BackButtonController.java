package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import org.example.javachessclient.Store;

public class BackButtonController {
    @FXML
    public void onBack() {
        Store.router.pop();
    }
}
