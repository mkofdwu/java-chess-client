package org.example.javachessclient.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;

import java.util.List;

public class HomeController {
    @FXML
    private GridPane ongoingGamesGrid;

    @FXML
    private GridPane gameRequestsGrid;

    public void initialize() {
        List<UserGame> userGames = Store.user.getOngoingGames();
        for (int i = 0; i < userGames.size(); ++i) {
            UserGame userGame = userGames.get(i);
            OngoingGame game = (OngoingGame) GameService.getGame(userGame.getGameId());

            Label gameLabel = new Label(userGame.getName());
            gameLabel.setOnMouseClicked((e) -> {
                Store.router.push("/fxml/game.fxml", userGame);
            });
            ongoingGamesGrid.add(gameLabel, 0, i);
            if ((game.getMoves().size() % 2 == 0) == userGame.getIsWhite()) {
                ongoingGamesGrid.add(new Label("Your turn"), 1, i);
            }
        }

        List<String> gameRequests = Store.user.getRequestsReceived();
        for (int i = 0; i < gameRequests.size(); ++i) {
            String otherUserId = gameRequests.get(i);
            UserProfile otherProfile = UserService.getUserProfile(otherUserId);

            Label usernameLabel = new Label(otherProfile.getUsername());
            ImageView acceptButton = new ImageView("/icons/accept.png");
            ImageView rejectButton = new ImageView("/icons/reject.png");
            acceptButton.setOnMouseClicked((e) -> {
                GameService.respondToGameRequest(otherUserId, true);
                gameRequestsGrid.getChildren().removeAll(usernameLabel, acceptButton, rejectButton);
            });
            rejectButton.setOnMouseClicked((e) -> {
                GameService.respondToGameRequest(otherUserId, false);
                gameRequestsGrid.getChildren().removeAll(usernameLabel, acceptButton, rejectButton);
            });
            gameRequestsGrid.addRow(i, usernameLabel, acceptButton, rejectButton);
        }
    }

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
