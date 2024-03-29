package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import org.example.javachessclient.Store;
import org.example.javachessclient.views.AboutModal;

public class LandingController {
    @FXML
    public void onStart() {
        Store.router.push("/fxml/login.fxml");
    }

    @FXML
    public void onShowAbout() {
        Store.modal.show(new AboutModal().getModal());
    }
}
