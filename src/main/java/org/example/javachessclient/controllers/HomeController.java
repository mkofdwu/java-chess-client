package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.example.javachessclient.Store;

public class HomeController {
    @FXML
    private GridPane ongoingGamesGrid;

    @FXML
    private GridPane gameRequestsGrid;

    @FXML
    public void onPlayRandom() {
        // TODO
        Store.router.push("/fxml/game.fxml");
    }

    @FXML
    public void onPlaySomeone() {
        // TODO
        Store.router.push("/fxml/play-with-someone.fxml");
    }
}
