package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Arrays;

public class LayoutController {
    @FXML
    public HBox rootHBox;
    @FXML
    public Label homeLabel;
    @FXML
    public Label historyLabel;
    @FXML
    public Label profileLabel;

    private Label[] allNavLabels;
    private static final String[] pageFxml = {"/fxml/home.fxml", "/fxml/history.fxml", "/fxml/profile.fxml"};

    private int selectedIndex;

    public void initialize() {
        allNavLabels = new Label[]{homeLabel, historyLabel, profileLabel};
        for (int i = 0; i < allNavLabels.length; ++i) {
            final int finalI = i;
            allNavLabels[i].setOnMouseClicked((e) -> loadPage(finalI));
        }
        loadPage(0); // load the first home page
    }

    private void loadPage(int i) {
        try {
            allNavLabels[selectedIndex].getStyleClass().remove("selected");
            allNavLabels[i].getStyleClass().add("selected");
            Parent page = FXMLLoader.load(getClass().getResource(pageFxml[i]));
            rootHBox.getChildren().set(1, page);
            selectedIndex = i;
        } catch (IOException exception) {
            System.out.println("Failed to load page: " + exception.getMessage());
        }
    }
}
