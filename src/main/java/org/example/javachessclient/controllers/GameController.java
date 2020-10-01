package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.socketgame.GameStompSessionHandler;
import org.example.javachessclient.socketgame.SocketGameService;
import org.example.javachessclient.socketgame.models.Message;
import org.example.javachessclient.socketgame.models.Move;
import org.springframework.messaging.simp.stomp.StompSession;

public class GameController {
    private Chess chess;
    private StompSession stompSession;
    private boolean waiting;
    private String otherUserId;

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
        chess = new Chess();
        stompSession = SocketGameService.createStompSession(this);
        // TODO: show loading screen until game is found

        box.getChildren().add(0, chess.getCanvas());
    }

    public void requestGame(String otherUserId) {
        // if otherUserId == null, random
        if (otherUserId == null) {
            stompSession.send("/socket/game/random");
        } else {
            stompSession.send("/socket/game/")
        }
    }

    public void onSocketNewGame(String otherUserId) {

    }

    public void onSocketMove(Move move) {

    }

    public void onSocketMessage(Message message) {
    }

    public void onSocketError(Throwable throwable) {
        // TODO: modal error message
    }

    @FXML
    void onRequestOfferDraw() {

    }

    @FXML
    void onRequestResign() {

    }
}
