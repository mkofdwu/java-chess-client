// fixme: this is an extremely messy & temporary implementation

package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.chess.enums.MoveType;
import org.example.javachessclient.chess.models.Move;
import org.example.javachessclient.chess.models.Square;
import org.example.javachessclient.chess.models.pieces.Piece;
import org.example.javachessclient.models.PastGame;
import org.example.javachessclient.models.RecordedMove;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.services.GameService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class PastGameController implements Controller {
    private Chess chess;
    private List<String> fenHistory;
    private int moveIndex = 0;

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
        UserGame userGame = (UserGame) data;
        PastGame game = GameService.getGame(userGame.getGameId(), PastGame.class);
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
            fenHistory.add(chess.toFEN());
            addMove(move);
            ++moveIndex;
        }
    }

    @FXML
    public void onRequestRename() {
    }

    @FXML
    public void onRequestSavePgn() {
//        FileChooser
//        chess.toPgn()
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
            recordTextArea.appendText((chess.getRecordedMoves().size() + 1) / 2 + ". " + move + " ");
        } else {
            recordTextArea.appendText(move + "\n");
        }
    }
}
