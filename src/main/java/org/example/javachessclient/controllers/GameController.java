package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Piece;
import org.example.javachessclient.chess.models.specialmoves.EnPassant;
import org.example.javachessclient.chess.models.specialmoves.SpecialEffect;
import org.example.javachessclient.models.*;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;

import java.text.ParseException;

public class GameController implements Controller {
    private static final String resignMessageText = "I resign";
    private static final String offerDrawMessageText = "Do you want a draw?";
    private static final String acceptDrawMessageText = "Sure";

    private Chess chess;
    private String otherUserId;
    private String gameId;
    private boolean isWhite;

    @FXML
    private Label gameNameLabel;

    @FXML
    private Label userUsernameLabel;

    @FXML
    private Label opponentUsernameLabel;

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

    @FXML
    private HBox userCapturedPiecesBox;

    @FXML
    private HBox opponentCapturedPiecesBox;

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
        OngoingGame game = GameService.getGame(userGame.getGameId(), OngoingGame.class);
        if (game == null) {
            Store.modal.showMessage("Error", "This game does not exist");
            Store.router.pop();
            return;
        }
        gameId = game.get_id();
        isWhite = userGame.getIsWhite();
        otherUserId = isWhite ? game.getBlack() : game.getWhite();
        UserProfile otherProfile = UserService.getUserProfile(otherUserId);
        try {
            gameNameLabel.setText(userGame.getName());
            userUsernameLabel.setText(Store.user.getUsername());
            opponentUsernameLabel.setText(otherProfile.getUsername());
            chess.loadFEN(Chess.startingFen);
            chess.setCanPlayWhite(isWhite);
            chess.setCanPlayBlack(!isWhite);
            if (!isWhite) chess.rotateBoard();
            box.getStyleClass().add(isWhite ? "white-player" : "black-player");
            // load recorded moves
            // fixme: refractor & clean
            for (RecordedMove recordedMove : game.getMoves()) {
                int fromFile = recordedMove.getFromFile();
                int fromRank = recordedMove.getFromRank();
                int toFile = recordedMove.getToFile();
                int toRank = recordedMove.getToRank();
                SpecialEffect specialEffect = SpecialEffect.fromString(chess, recordedMove.getSpecialEffectString());
                Piece piece = chess.pieceAt(fromFile, fromRank);
                Move move = new Move(
                        piece,
                        new Square(fromFile, fromRank),
                        new Square(toFile, toRank),
                        specialEffect instanceof EnPassant
                                ? chess.pieceAt(toFile, toRank + (piece.getIsWhite() ? -1 : 1))
                                : chess.pieceAt(toFile, toRank),
                        specialEffect
                );
                chess.playMove(move);
                addMove(move);
            }
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
                move.getSpecialEffect().toString(),
                chess.getResult(),
                chess.getNotationParser().toFEN()
        );
        addMove(move);
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
            SpecialEffect specialEffect = SpecialEffect.fromString(chess, socketMove.getSpecialEffectString());
            Piece piece = chess.pieceAt(fromFile, fromRank);
            Move move = new Move(
                    piece,
                    new Square(fromFile, fromRank),
                    new Square(toFile, toRank),
                    specialEffect instanceof EnPassant
                            ? chess.pieceAt(toFile, toRank + (piece.getIsWhite() ? -1 : 1))
                            : chess.pieceAt(toFile, toRank),
                    specialEffect
            );
            chess.playMove(move);
            addMove(move);
        }
    }

    public void onSocketMessage(SocketMessage message) {
        if (message.getGameId().equals(gameId)) {
            addMessage(message.getText(), false);

            String messageText = message.getText();
            if (messageText.equals(resignMessageText)) {
                Store.modal.showMessage("Congratulations!", "Your opponent has resigned and you win the game.");
                chess.setResult(isWhite ? 2 : 3);
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
                                chess.setResult(1);
                                chess.setCanPlayWhite(false);
                                chess.setCanPlayBlack(false);
                            }
                        }
                );
            } else if (messageText.equals(acceptDrawMessageText)) {
                Label prevMessage = (Label) ((HBox) messagesBox.getChildren().get(messagesBox.getChildren().size() - 2)).getChildren().get(0);
                if (prevMessage.getTextAlignment() == TextAlignment.RIGHT && prevMessage.getText().equals(offerDrawMessageText)) {
                    // the above checks for message sent by me requesting for a draw
                    Store.modal.showMessage("It's a draw", "Your opponent accepted your draw offer");
                    chess.setResult(1);
                    chess.setCanPlayWhite(false);
                    chess.setCanPlayBlack(false);
                }
            }
        }
    }

    @FXML
    void onSendMessage() {
        sendMessage(messageInput.getText());
        messageInput.setText("");
    }

    @FXML
    void onRequestOfferDraw() {
        if (chess.getResult() == 0) {
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
    }

    @FXML
    void onRequestResign() {
        if (chess.getResult() == 0) {
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
    }

    @FXML
    void onRotateBoard() {
        chess.rotateBoard();
    }

    private void addMove(Move move) {
        // update recording
        if (move.getPiece().getIsWhite()) {
            recordTextArea.appendText(chess.getMoves().size() + ". " + move + " ");
        } else {
            recordTextArea.appendText(move + "\n");
        }
        // update captured pieces
        if (move.getCapturedPiece() != null) {
            ImageView pieceImage = new ImageView(getClass().getResource(move.getCapturedPiece().getIconFilePath()).toExternalForm());
            pieceImage.setFitWidth(24);
            pieceImage.setFitHeight(24);
            HBox capturedPiecesBox = (move.getCapturedPiece().getIsWhite() == isWhite)
                    ? opponentCapturedPiecesBox
                    : userCapturedPiecesBox;
            if (capturedPiecesBox.getChildren().get(0) instanceof Label) {
                // remove placeholder label
                capturedPiecesBox.getChildren().remove(0);
            }
            capturedPiecesBox.getChildren().add(pieceImage);
        }
    }

    private void addMessage(String text, boolean fromMe) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(fromMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(fromMe ? TextAlignment.RIGHT : TextAlignment.LEFT);
        messageBox.getChildren().add(messageLabel);
        messagesBox.getChildren().add(messageBox);
    }

    private void sendMessage(String text) {
        addMessage(text, true);
        Store.stompSession.send("/app/message/" + otherUserId, new SocketMessage(gameId, Store.user.get_id(), text));
    }
}
