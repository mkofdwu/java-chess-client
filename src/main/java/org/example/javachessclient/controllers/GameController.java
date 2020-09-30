package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.javachessclient.chess.Chess;

public class GameController {
    private final Chess chess = new Chess();

    @FXML
    private Label gameNameLabel;

    @FXML
    private HBox box;

    @FXML
    private Pane recordPane;

    @FXML
    private VBox chatPane;

    @FXML
    private VBox optionsPane;

    public void initialize() {
        box.getChildren().add(0, chess.getCanvas());
    }

    @FXML
    void onRequestOfferDraw() {

    }

    @FXML
    void onRequestResign() {

    }
}
