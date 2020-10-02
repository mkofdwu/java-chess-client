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
    private static final SimpleDateFormat gameDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // FIXME: improve date format

    @FXML
    private GridPane pastGamesGrid;

    public void initialize() {
        List<UserGame> pastGames = Store.user.getPastGames();
        for (int i = 0; i < pastGames.size(); ++i) {
            UserGame userGame = pastGames.get(i);
            PastGame game = (PastGame) GameService.getGame(userGame.getGameId());
            pastGamesGrid.add(new Label(userGame.getName()), i, 0);
            pastGamesGrid.add(new Label(formatGameResult(game.getResult())), i, 1);
            pastGamesGrid.add(new Label(gameDateFormat.format(game.getTimestamp())), i, 2);
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
