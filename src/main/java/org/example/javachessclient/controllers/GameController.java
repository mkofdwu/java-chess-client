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
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;
import org.example.javachessclient.models.SocketMessage;
import org.example.javachessclient.models.SocketMove;

import java.text.ParseException;

public class GameController implements Controller {
    private static final String resignMessageText = "I resign";
    private static final String offerDrawMessageText = "Do you want a draw?";
    private static final String acceptDrawMessageText = "Sure";

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
        Parent[] parents = new Parent[]{recordTextArea, chatPane, optionsPane};

        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            allTabs[i].setOnMouseClicked((e) -> {
                allTabs[selectedIndex].getStyleClass().remove("selected");
                allTabs[finalI].getStyleClass().add("selected");
                parents[selectedIndex].setVisible(false);
                parents[finalI].setVisible(true);
                selectedIndex = finalI;
            });
        }
    }

    @Override
    public void loadData(Object data) {
        UserGame userGame = (UserGame) data;
        OngoingGame game = (OngoingGame) GameService.getGame(userGame.getGameId());
        gameId = game.get_id();
        boolean isWhite = userGame.getIsWhite();
        otherUserId = isWhite ? game.getBlack() : game.getWhite();
        UserProfile otherProfile = UserService.getUserProfile(otherUserId);
        try {
            gameNameLabel.setText(userGame.getName());
            whiteUsernameLabel.setText(isWhite ? Store.user.getUsername() : otherProfile.getUsername());
            blackUsernameLabel.setText(isWhite ? otherProfile.getUsername() : Store.user.getUsername());
            chess.loadFEN(game.getFenPosition());
            chess.setCanPlayWhite(isWhite);
            chess.setCanPlayBlack(!isWhite);
            if (!isWhite) chess.rotateBoard();
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
            // play a move received from the other player
            int fromFile = socketMove.getFromFile();
            int fromRank = socketMove.getFromRank();
            int toFile = socketMove.getToFile();
            int toRank = socketMove.getToRank();
            chess.playMove(new Move(
                    chess.pieceAt(socketMove.getFromFile(), socketMove.getFromRank()),
                    new Square(fromFile, fromRank),
                    new Square(toFile, toRank),
                    Enum.valueOf(MoveType.class, socketMove.getMoveType()),
                    chess.pieceAt(toFile, toRank)
            ));
        }
    }

    public void onSocketMessage(SocketMessage message) {
        if (message.getGameId().equals(gameId)) {
            addMessage(message.getText(), false);

            String messageText = message.getText();
            if (messageText.equals(resignMessageText)) {
                Store.modal.showMessage("Congratulations!", "Your opponent has resigned and you win the game.");
                chess.setCanPlayWhite(false);
                chess.setCanPlayBlack(false);
            } else if (messageText.equals(offerDrawMessageText)) {
                Store.modal.showOptions(
                        "Accept draw?",
                        "Your opponent has offered a draw. Accept it?",
                        new String[]{"Yes", "No"},
                        (option) -> {
                            if (option.equals("Yes")) {
                                sendMessage("Sure");
                                chess.setCanPlayWhite(false);
                                chess.setCanPlayBlack(false);
                            }
                        }
                );
            } else if (messageText.equals(acceptDrawMessageText)) {
                Label prevMessage = (Label) messagesBox.getChildren().get(messagesBox.getChildren().size() - 2);
                if (prevMessage.getTextAlignment() == TextAlignment.RIGHT && prevMessage.getText().equals(offerDrawMessageText)) {
                    // the above checks for message sent by me requesting for a draw
                    Store.modal.showMessage("It's a draw", "Your opponent accepted your draw offer");
                    chess.setCanPlayWhite(false);
                    chess.setCanPlayBlack(false);
                }
            }
        }
    }

    @FXML
    void onSendMessage() {
        sendMessage(messageInput.getText());
    }

    @FXML
    void onRequestOfferDraw() {
        Store.modal.showOptions(
                "Offer draw?",
                "Are you sure you want to offer a draw?",
                new String[]{"Yes", "No"},
                (option) -> {
                    if (option.equals("Yes")) {
                        sendMessage(offerDrawMessageText);
                    }
                }
        );
    }

    @FXML
    void onRequestResign() {
        Store.modal.showOptions(
                "Concede match?",
                "Are you sure you want to resign and concede the match?",
                new String[]{"Yes", "No"},
                (option) -> {
                    if (option.equals("Yes")) {
                        sendMessage(resignMessageText);
                        chess.setCanPlayWhite(false);
                        chess.setCanPlayBlack(false);
                    }
                }
        );
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

    private void sendMessage(String text) {
        addMessage(text, true);
        Store.stompSession.send("/app/message/" + otherUserId, new SocketMessage(gameId, Store.user.get_id(), text));
    }
}
