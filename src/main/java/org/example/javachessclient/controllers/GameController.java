package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.socketgame.models.Message;
import org.example.javachessclient.socketgame.models.SocketMove;

import java.text.ParseException;

public class GameController implements Controller {
    private Chess chess;
    private String otherUserId;
    private String gameId;

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
        Store.gameController = this;
        box.getChildren().add(0, chess.getCanvas());
    }

    @Override
    public void loadData(Object data) {
        UserGame userGame = (UserGame) data;
        OngoingGame game = (OngoingGame) GameService.getGame(userGame.getGameId());
        otherUserId = userGame.getIsWhite() ? game.getBlack() : game.getWhite();
        gameId = game.getId();
        try {
            gameNameLabel.setText(userGame.getName());
            chess.loadFEN(game.getFenPosition());
            // TODO: more loading based on the game object
        } catch (ParseException exception) {
            Store.modal.showMessage("Error", "Error parsing fen string: " + game.getFenPosition());
        }
    }

    public void onSocketMove(SocketMove socketMove) {
        // check if move is made in THIS game
        if (socketMove.getGameId().equals(gameId)) {
            chess.playSocketMove(socketMove);
        }
    }

    public void onSocketMessage(Message message) {
        if (message.getGameId().equals(gameId)) {
            // TODO
        }
    }

    public void onSocketError(Throwable throwable) {
        Store.modal.showMessage("Error", "An unexpected websocket error has occurred: " + throwable.getMessage());
    }

    @FXML
    void onRequestOfferDraw() {

    }

    @FXML
    void onRequestResign() {

    }
}
