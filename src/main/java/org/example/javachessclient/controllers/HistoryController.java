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
        for (int i = 0; i < pastGames.size(); ++i) {
            UserGame userGame = pastGames.get(i);
            PastGame game = (PastGame) GameService.getGame(userGame.getGameId());
            Label gameLabel = new Label(userGame.getName());
            gameLabel.setOnMouseClicked((e) -> {
                Store.router.push("/fxml/game.fxml", userGame);
            });
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
        if (result == 1) {
            return "White wins";
        }
        if (result == -1) {
            return "Black wins";
        }
        return "Draw";
    }
}
