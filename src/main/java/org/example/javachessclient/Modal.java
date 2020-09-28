package org.example.javachessclient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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
        modal.setMaxHeight(200);
        modal.setPadding(new Insets(20, 20, 20, 20));
        modal.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Domaine Display Test", 18));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font("Domaine Display Test", 14));

        VBox optionsBox = new VBox();
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction((e) -> hide());
        optionsBox.getChildren().add(closeBtn);

        modal.getChildren().addAll(
                titleLabel,
                messageLabel,
                optionsBox
        );

        show(modal);
    }
}
