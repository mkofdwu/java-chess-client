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
import org.example.javachessclient.models.ModalCallback;

public class Modal {
    private final Pane bgPane;

    public Modal(StackPane rootPane) {
        bgPane = new Pane();
        bgPane.setStyle("-fx-background-color: #000000aa");
        bgPane.setVisible(false);
        rootPane.getChildren().add(bgPane);
    }

    public void show(VBox modal) {
        modal.layoutXProperty().bind(bgPane.widthProperty().subtract(modal.widthProperty()).divide(2));
        modal.layoutYProperty().bind(bgPane.heightProperty().subtract(modal.heightProperty()).divide(2));
        bgPane.getChildren().add(modal);
        bgPane.setVisible(true);
    }

    public void hide() {
        bgPane.setVisible(false);
    }

    public void showMessage(String title, String message) {
        VBox modal = new VBox();
        modal.setPrefWidth(400);
        modal.setPadding(new Insets(20, 24, 20, 24));
        modal.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        VBox content = new VBox();
        content.setAlignment(Pos.TOP_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Domaine Display Test", 20));
        titleLabel.setStyle("-fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(0, 0, 10, 0));
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setFont(new Font("Domaine Display Test", 14));
        content.getChildren().addAll(titleLabel, messageLabel);
        VBox.setMargin(content, new Insets(0, 0, 40, 0));

        VBox optionsBox = new VBox();
        optionsBox.setAlignment(Pos.BOTTOM_RIGHT);
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: -fx-accent, white;" +
                "-fx-background-insets: 0 -1 -2 -1, 0 -1 0 -1;" +
                "-fx-background-radius: 0;" +
                "-fx-text-fill: -fx-accent;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: \"Domaine Text Test\";" +
                "-fx-padding: 0;" +
                "-fx-cursor: hand;");
        closeBtn.setOnAction((e) -> hide());
        optionsBox.getChildren().add(closeBtn);

        modal.getChildren().addAll(
                content,
                optionsBox
        );

        show(modal);
    }

    public void showQuestion(String title, String message, String[] options, ModalCallback cb) {
        // TODO

    }
}
