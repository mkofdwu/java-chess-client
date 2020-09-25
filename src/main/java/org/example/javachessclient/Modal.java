package org.example.javachessclient;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

    public void showMessage(String message) {
        // FIXME
        Pane pane = new Pane();
        pane.setPrefSize(300, 200);
        pane.getChildren().add(new Label(message));
        show(pane);
    }
}
