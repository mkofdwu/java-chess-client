package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.GameService;

public class SearchingForGameController {
    @FXML
    void onCancelSearch() {
        GameService.cancelSearchForRandomGame();
        Store.router.pop();
    }
}
