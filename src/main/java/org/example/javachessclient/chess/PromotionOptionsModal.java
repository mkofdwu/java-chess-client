package org.example.javachessclient.chess;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.ModalCallback;

public class PromotionOptionsModal {
    public VBox buildModal(ModalCallback cb) {
        VBox modal = new VBox();
        modal.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        modal.setPrefWidth(400);
        modal.setPadding(new Insets(20, 24, 20, 24));
        modal.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        Label titleLabel = new Label("Promote to another piece");
        titleLabel.setStyle("-fx-font-family: \"Domaine Display Test\"; -fx-font-size: 20px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(0, 0, 20, 0));

        modal.getChildren().add(titleLabel);

        for (String pieceName : new String[]{"Queen", "Rook", "Bishop", "Knight"}) {
            Button pieceOptionBtn = new Button(pieceName);
            pieceOptionBtn.setMaxWidth(Double.MAX_VALUE);
            pieceOptionBtn.setOnMouseClicked((e) -> {
                Store.modal.hide();
                cb.callback(pieceName);
            });
            VBox.setMargin(pieceOptionBtn, new Insets(0, 0, 14, 0));
            modal.getChildren().add(pieceOptionBtn);
        }

        return modal;
    }
}
