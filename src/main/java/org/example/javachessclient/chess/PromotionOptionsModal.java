package org.example.javachessclient.chess;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class PromotionOptionsModal {
    public Pane buildPane() {
        Pane pane = new Pane();
        pane.setPrefSize(400, 150);

        Button queenBtn = new Button("Queen");
        Button rookBtn = new Button("Rook");
        Button bishopBtn = new Button("Bishop");
        Button knightBtn = new Button("Knight");

        pane.getChildren().addAll(queenBtn, rookBtn, bishopBtn, knightBtn);
        return pane;
    }
}
