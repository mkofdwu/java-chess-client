package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ProfileController {
    @FXML
    public ImageView profilePicView;

    public void initialize() {
        profilePicView.setClip(new Circle(90));
    }
}

