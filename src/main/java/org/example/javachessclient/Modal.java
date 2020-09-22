package org.example.javachessclient;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class Modal {
    private StackPane bgPane;

    public Modal(StackPane rootPane) {
        bgPane = new StackPane();
        bgPane.setStyle("-fx-background-color: #000000aa");
        bgPane.setVisible(false);
        rootPane.getChildren().add(bgPane);
    }

    public void show(Parent parent) {
        bgPane.getChildren().add(parent);
        StackPane.setAlignment(parent, Pos.CENTER);
        bgPane.setVisible(true);
    }

    public void showMessage() {
        // TODO
    }
}
