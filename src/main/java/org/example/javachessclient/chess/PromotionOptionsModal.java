package org.example.javachessclient.chess;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PromotionOptionsModal {
    public VBox buildModal() {
        VBox modal = new VBox();
        modal.setPrefSize(400, 150);

        Button queenBtn = new Button("Queen");
        Button rookBtn = new Button("Rook");
        Button bishopBtn = new Button("Bishop");
        Button knightBtn = new Button("Knight");

        modal.getChildren().addAll(new Label("Promotion"), queenBtn, rookBtn, bishopBtn, knightBtn);
        return modal;
    }
}
