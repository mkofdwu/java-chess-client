// fixme: this is an extremely messy & temporary implementation

package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Piece;
import org.example.javachessclient.models.*;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;
import org.example.javachessclient.views.TextInputModal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PastGameController implements Controller {
    private Chess chess;
    private UserGame userGame;
    private PastGame game;
    private List<String> fenHistory;
    private int moveIndex = 0;

    @FXML
    private Label gameNameLabel;

    @FXML
    private HBox box;

    @FXML
    private TextArea recordTextArea;

    public void initialize() {
        chess = new Chess();
        fenHistory = new ArrayList<>();
        box.getChildren().set(0, chess.getCanvas());
    }

    @Override
    public void loadData(Object data) {
        userGame = (UserGame) data;
        gameNameLabel.setText(userGame.getName());
        game = GameService.getGame(userGame.getGameId(), PastGame.class);
        try {
            chess.loadFEN(Chess.startingFen);
        } catch (ParseException exception) {
            System.out.println("failed to parse starting fen: " + exception.getMessage());
            return;
        }
        fenHistory.add(Chess.startingFen);
        for (RecordedMove recordedMove : game.getMoves()) {
            int fromFile = recordedMove.getFromFile();
            int fromRank = recordedMove.getFromRank();
            int toFile = recordedMove.getToFile();
            int toRank = recordedMove.getToRank();
            MoveType type = Enum.valueOf(MoveType.class, recordedMove.getMoveType());
            Piece piece = chess.pieceAt(fromFile, fromRank);
            Move move = new Move(
                    piece,
                    new Square(fromFile, fromRank),
                    new Square(toFile, toRank),
                    type,
                    type == MoveType.enPassant ? chess.pieceAt(toFile, toRank + (piece.getIsWhite() ? -1 : 1)) : chess.pieceAt(toFile, toRank)
            );
            chess.playMove(move);
            fenHistory.add(chess.getNotationParser().toFEN());
            addMove(move);
            ++moveIndex;
        }
    }

    @FXML
    public void onRequestRename() {
        Store.modal.show(TextInputModal.buildModal(
                "Rename game",
                (newName) -> {
                    if (newName != null) {
                        UserService.updateUserGame(game.get_id(), new UserGameUpdateDetails(newName));
                        gameNameLabel.setText(newName);
                    }
                }
        ));
    }

    @FXML
    public void onRequestSavePgn() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Game Notation", "*.pgn"));
        File file = fileChooser.showSaveDialog(box.getScene().getWindow());

        if (file != null) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(chess.getNotationParser().toPgn(userGame, game));
                bw.close();
            } catch (IOException exception) {
                System.out.println("Failed to save pgn");
                exception.printStackTrace();
            }
        }
    }

    @FXML
    public void onPrevMove() {
        if (moveIndex > 0) {
            try {
                chess.loadFEN(fenHistory.get(--moveIndex));
            } catch (ParseException exception) {
                System.out.println("Failed to parse FEN: " + fenHistory.get(moveIndex));
            }
        }
    }

    @FXML
    public void onNextMove() {
        if (moveIndex < fenHistory.size() - 1) {
            try {
                chess.loadFEN(fenHistory.get(++moveIndex));
            } catch (ParseException exception) {
                System.out.println("Failed to parse FEN: " + fenHistory.get(moveIndex));
            }
        }
    }

    private void addMove(Move move) {
        // update recording
        if (move.getPiece().getIsWhite()) {
            recordTextArea.appendText((chess.getMoves().size() + 1) / 2 + ". " + move + " ");
        } else {
            recordTextArea.appendText(move + "\n");
        }
    }
}
