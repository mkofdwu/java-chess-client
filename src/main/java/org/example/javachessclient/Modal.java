package org.example.javachessclient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    public void showOptions(String title, String message, String[] options, ModalCallback cb) {
        VBox modal = new VBox();
        modal.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        modal.setPrefWidth(400);
        modal.setPadding(new Insets(20, 24, 20, 24));
        modal.setStyle("-fx-background-color: -fx-absolute; -fx-background-radius: 5px;");

        // content.setAlignment(Pos.TOP_LEFT);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-family: \"Domaine Display Test\"; -fx-font-size: 20px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(0, 0, 10, 0));
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-family: \"Domaine Display Test\";");
        VBox.setMargin(messageLabel, new Insets(0, 0, 40, 0));

        HBox optionsBox = new HBox();
        optionsBox.setSpacing(20);
        optionsBox.setAlignment(Pos.BOTTOM_RIGHT);
        for (String option : options) {
            Button optionBtn = new Button(option);
            optionBtn.setStyle("-fx-background-color: -fx-accent, -fx-absolute;" +
                    "-fx-background-insets: 0 -1 -1 -1, 0 -1 0 -1;" +
                    "-fx-background-radius: 0;" +
                    "-fx-text-fill: -fx-accent;" +
                    "-fx-padding: 0;" +
                    "-fx-cursor: hand;");
            optionBtn.setOnMouseClicked((e) -> {
                hide();
                cb.callback(option);
            });
            optionsBox.getChildren().add(optionBtn);
        }

        modal.getChildren().addAll(titleLabel, messageLabel, optionsBox);
        show(modal);
    }

    public void showMessage(String title, String message) {
        showOptions(title, message, new String[]{"Close"}, (option) -> {
        });
    }
}
