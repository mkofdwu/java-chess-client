package org.example.javachessclient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Modal {
    private final StackPane bgPane;

    public Modal(StackPane rootPane) {
        bgPane = new StackPane();
        bgPane.setStyle("-fx-background-color: #000000bb");
        bgPane.setVisible(false);
        rootPane.getChildren().add(bgPane);
    }

    public void show(Parent parent) {
        bgPane.getChildren().add(parent);
        StackPane.setAlignment(parent, Pos.CENTER);
        bgPane.setVisible(true);
    }

    public void hide() {
        bgPane.setVisible(false);
    }

    public void showMessage(String title, String message) {
        // FIXME
        VBox modal = new VBox();
        modal.setMaxWidth(400);
        modal.setPadding(new Insets(20, 20, 20, 20));
        modal.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Domaine Display Test", 18));
        titleLabel.setStyle("-fx-font-weight: medium;");

        Label messageLabel = new Label(message);


        modal.getChildren().addAll(
                titleLabel,
                messageLabel
        );

        show(modal);
    }
}
