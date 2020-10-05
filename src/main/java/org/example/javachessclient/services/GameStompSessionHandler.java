package org.example.javachessclient.services;

import javafx.application.Platform;
import org.example.javachessclient.Store;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;
import org.example.javachessclient.models.SocketMessage;
import org.example.javachessclient.models.SocketMove;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.Objects;

public class GameStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        stompSession.subscribe("/topic/new-games/" + Store.user.get_id(), this);
        stompSession.subscribe("/topic/requests/" + Store.user.get_id(), this);
        stompSession.subscribe("/topic/accepted-requests/" + Store.user.get_id(), this);
        stompSession.subscribe("/topic/moves/" + Store.user.get_id(), this);
        stompSession.subscribe("/topic/messages/" + Store.user.get_id(), this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        Platform.runLater(() -> Store.modal.showMessage("Error", "Websockets encountered an exception: " + throwable.getMessage()));
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        Platform.runLater(() -> Store.modal.showMessage("Error", "Websockets encountered a transport error: " + throwable.getMessage()));
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        String destination = Objects.requireNonNull(stompHeaders.getDestination()); // autosuggested
        if (destination.startsWith("/topic/requests")) {
            return String.class;
        }
        if (destination.startsWith("/topic/new-games") || destination.startsWith("/topic/accepted-requests")) {
            return UserGame.class;
        }
        if (destination.startsWith("/topic/moves")) {
            return SocketMove.class;
        }
        if (destination.startsWith("/topic/messages")) {
            return SocketMessage.class;
        }
        throw new RuntimeException("Invalid destination: " + destination);
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        String destination = Objects.requireNonNull(stompHeaders.getDestination()); // autosuggested
        if (destination.startsWith("/topic/new-games")) {
            Platform.runLater(() -> Store.router.push("/fxml/game.fxml", payload));
        } else if (destination.startsWith("/topic/requests")) {
            String otherUserId = (String) payload;
            UserProfile otherProfile = UserService.getUserProfile(otherUserId);
            Platform.runLater(() -> Store.modal.showOptions(
                    "Game request",
                    otherProfile.getUsername() + " wants to play chess with you.",
                    new String[]{"Ok fine", "No", "Decide later"},
                    (option) -> {
                        if (option.equals("Ok fine")) {
                            GameService.respondToGameRequest(otherUserId, true);
                        } else if (option.equals("No")) {
                            GameService.respondToGameRequest(otherUserId, false);
                        }
                    }
            ));
        } else if (destination.startsWith("/topic/accepted-requests")) {
            UserGame userGame = (UserGame) payload;
            OngoingGame game = (OngoingGame) GameService.getGame(userGame.getGameId());
            String otherUserId = userGame.getIsWhite() ? game.getBlack() : game.getWhite();
            UserProfile otherProfile = UserService.getUserProfile(otherUserId);
            Platform.runLater(() -> Store.modal.showOptions(
                    "Request accepted",
                    otherProfile.getUsername() + " agreed to play chess with you. Start the game now?",
                    new String[]{"Sure", "Later"},
                    (option) -> {
                        if (option.equals("Sure")) {
                            Store.router.push("/fxml/game.fxml", userGame);
                        }
                    }
            ));
        } else if (destination.startsWith("/topic/moves")) {
            if (Store.gameController != null)
                Store.gameController.onSocketMove((SocketMove) payload);
        } else if (destination.startsWith("/topic/messages")) {
            if (Store.gameController != null)
                Store.gameController.onSocketMessage((SocketMessage) payload);
        } else {
            throw new RuntimeException("Invalid destination: " + destination);
        }
    }
}
