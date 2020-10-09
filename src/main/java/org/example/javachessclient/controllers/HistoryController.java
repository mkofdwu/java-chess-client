package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.PastGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.services.GameService;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryController {
    private static final SimpleDateFormat gameDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private GridPane pastGamesGrid;

    public void initialize() {
        List<UserGame> pastGames = Store.user.getPastGames();
        if (!pastGames.isEmpty()) {
            pastGamesGrid.getChildren().remove(0); // remove placeholder label, the first child
        }
        for (int i = 0; i < pastGames.size(); ++i) {
            UserGame userGame = pastGames.get(i);
            PastGame game = GameService.getGame(userGame.getGameId(), PastGame.class);
            Label gameLabel = new Label(userGame.getName());
            gameLabel.setStyle("-fx-cursor: hand;");
            gameLabel.setOnMouseClicked((e) -> Store.router.push("/fxml/past-game.fxml", userGame));
            pastGamesGrid.addRow(
                    i,
                    gameLabel,
                    new Label(formatGameResult(game.getResult())),
                    new Label(gameDateFormat.format(game.getTimestamp()))
            );
        }
    }

    // utils

    private String formatGameResult(int result) {
        if (result == 0) {
            return "*";
        }
        if (result == 1) {
            return "1/2 - 1/2";
        }
        if (result == 2) {
            return "1 - 0";
        }
        if (result == 3) {
            return "0 - 1";
        }
        return "?";
    }
}
