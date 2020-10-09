package org.example.javachessclient.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.views.PromotionOptionsModal;
import org.example.javachessclient.models.ModalCallback;

public class TextInputModal {
    public static VBox buildModal(String title, ModalCallback cb) {
        VBox modal = new VBox();
        modal.getStylesheets().add(PromotionOptionsModal.class.getResource("/styles/main.css").toExternalForm());
        modal.setPrefWidth(400);
        modal.setPadding(new Insets(20, 24, 20, 24));
        modal.setStyle("-fx-background-color: -fx-absolute; -fx-background-radius: 5px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-family: \"Domaine Display Test\"; -fx-font-size: 20px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(0, 0, 10, 0));

        TextField input = new TextField();
        input.setStyle("-fx-background-color: -fx-lesser, -fx-absolute; -fx-background-insets: 0 -1 -1 -1, 0 -1 0 -1;");
        VBox.setMargin(input, new Insets(0, 0, 40, 0));

        HBox optionsBox = new HBox();
        optionsBox.setSpacing(20);
        optionsBox.setAlignment(Pos.BOTTOM_RIGHT);

        Button okBtn = new Button("Ok");
        okBtn.getStyleClass().add("underline-btn");
        okBtn.setOnMouseClicked((e) -> {
            Store.modal.hide();
            cb.callback(input.getText());
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("underline-btn");
        cancelBtn.setOnMouseClicked((e) -> {
            Store.modal.hide();
            cb.callback(null);
        });

        optionsBox.getChildren().addAll(okBtn, cancelBtn);

        modal.getChildren().addAll(titleLabel, input, optionsBox);

        return modal;
    }
}
