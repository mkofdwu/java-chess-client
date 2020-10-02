package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.GameService;

public class HomeController {
    @FXML
    private GridPane ongoingGamesGrid;

    @FXML
    private GridPane gameRequestsGrid;

    @FXML
    public void onPlayRandom() {
        GameService.randomGame();
        Store.router.push("/fxml/searching-for-game.fxml");
    }

    @FXML
    public void onPlaySomeone() {
        Store.router.push("/fxml/play-with-someone.fxml");
    }
}
