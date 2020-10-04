package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;
import org.example.javachessclient.socketgame.models.SocketMessage;
import org.example.javachessclient.socketgame.models.SocketMove;

import java.text.ParseException;

public class GameController implements Controller {
    private Chess chess;
    private String otherUserId;
    private String gameId;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label whiteUsernameLabel;

    @FXML
    private Label blackUsernameLabel;

    @FXML
    private HBox box;

    @FXML
    private TextArea recordTextArea;

    @FXML
    private VBox chatPane;

    @FXML
    private VBox messagesBox;

    @FXML
    private TextField messageInput;

    @FXML
    private VBox optionsPane;

    @FXML
    private Pane recordTab;

    @FXML
    private Pane chatTab;

    @FXML
    private Pane optionsTab;

    private int selectedIndex = 0;

    public void initialize() {
        Store.gameController = this;

        chess = new Chess();
        chess.setOnUserMove(this::onUserMove);
        box.getChildren().set(0, chess.getCanvas());

        Pane[] allTabs = new Pane[]{recordTab, chatTab, optionsTab};
        Parent[] parent = new Parent[]{recordTextArea, chatPane, optionsPane};

        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            allTabs[i].setOnMouseClicked((e) -> {
                allTabs[selectedIndex].getStyleClass().remove("selected");
                allTabs[finalI].getStyleClass().add("selected");
                parent[selectedIndex].setVisible(false);
                parent[selectedIndex].setVisible(true);
                selectedIndex = finalI;
            });
        }
    }

    @Override
    public void loadData(Object data) {
        UserGame userGame = (UserGame) data;
        OngoingGame game = (OngoingGame) GameService.getGame(userGame.getGameId());
        gameId = game.getId();
        otherUserId = userGame.getIsWhite() ? game.getBlack() : game.getWhite();
        UserProfile otherProfile = UserService.getUserProfile(otherUserId);
        try {
            gameNameLabel.setText(userGame.getName());
            whiteUsernameLabel.setText(userGame.getIsWhite() ? Store.user.getUsername() : otherProfile.getUsername());
            blackUsernameLabel.setText(userGame.getIsWhite() ? otherProfile.getUsername() : Store.user.getUsername());
            chess.loadFEN(game.getFenPosition());
            if (!userGame.getIsWhite()) chess.rotateBoard();
        } catch (ParseException exception) {
            Store.modal.showMessage("Error", "Error parsing fen string: " + game.getFenPosition());
        }
    }

    public void onUserMove(Move move) {
        Square fromSquare = move.getFromSquare();
        Square toSquare = move.getToSquare();
        SocketMove socketMove = new SocketMove(
                gameId,
                fromSquare.getFile(),
                fromSquare.getRank(),
                toSquare.getFile(),
                toSquare.getRank(),
                move.getType().name(),
                chess.toFEN()
        );
        Store.stompSession.send("/app/move/" + otherUserId, socketMove);
    }

    public void onSocketMove(SocketMove socketMove) {
        // check if move is made in THIS game
        if (socketMove.getGameId().equals(gameId)) {
            chess.playSocketMove(socketMove);
        }
    }

    public void onSocketMessage(SocketMessage message) {
        if (message.getGameId().equals(gameId)) {
            addMessage(message.getText(), false);
        }
    }

    @FXML
    void onSendMessage() {
        addMessage(messageInput.getText(), true);
        Store.stompSession.send("/app/message/" + otherUserId, new SocketMessage(gameId, Store.user.get_id(), messageInput.getText()));
    }

    @FXML
    void onRequestOfferDraw() {
        // TODO
    }

    @FXML
    void onRequestResign() {
        // TODO
        // Store.modal.showQuestion();
    }

    @FXML
    void onRotateBoard() {
        chess.rotateBoard();
    }

    private void addMessage(String text, boolean fromMe) {
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(fromMe ? TextAlignment.RIGHT : TextAlignment.LEFT);
        messagesBox.getChildren().add(messageLabel);
    }
}
