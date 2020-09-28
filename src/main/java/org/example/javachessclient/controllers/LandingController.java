package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import org.example.javachessclient.Store;

public class LandingController {
    @FXML
    public void onStart() {
        Store.router.push("/fxml/login.fxml");
    }
}
