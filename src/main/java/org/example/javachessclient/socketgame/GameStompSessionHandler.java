package org.example.javachessclient.socketgame;

import org.example.javachessclient.Store;
import org.example.javachessclient.models.OngoingGame;
import org.example.javachessclient.models.UserGame;
import org.example.javachessclient.models.UserProfile;
import org.example.javachessclient.services.GameService;
import org.example.javachessclient.services.UserService;
import org.example.javachessclient.socketgame.models.Message;
import org.example.javachessclient.socketgame.models.SocketMove;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.Objects;

public class GameStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        stompSession.subscribe("/topic/new-games", this);
        stompSession.subscribe("/topic/requests", this);
        stompSession.subscribe("/topic/accepted-requests", this);

        stompSession.subscribe("/topic/moves", this);
        stompSession.subscribe("/topic/messages", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        Store.gameController.onSocketError(throwable);
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        Store.gameController.onSocketError(throwable);
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        switch (Objects.requireNonNull(stompHeaders.getSubscription())) { // autosuggested
            case "/topic/new-games":
            case "/topic/requests":
            case "/topic/accepted-requests":
                return String.class;
            case "/topic/moves":
                return SocketMove.class;
            case "/topic/messages":
                return Message.class;
        }
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        switch (Objects.requireNonNull(stompHeaders.getSubscription())) { // autosuggested
            case "/topic/new-games":
                Store.router.push("/fxml/game.fxml", payload);
                break;
            case "/topic/requests":
                String otherUserId = (String) payload;
                UserProfile otherProfile = UserService.getUserProfile(otherUserId);
                Store.modal.showQuestion(
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
                );
                break;
            case "/topic/accepted-requests":
                UserGame userGame = (UserGame) payload;
                OngoingGame game = (OngoingGame) GameService.getGame(userGame.getGameId());
                otherUserId = userGame.getIsWhite() ? game.getBlack() : game.getWhite();
                otherProfile = UserService.getUserProfile(otherUserId);
                Store.modal.showQuestion(
                        "Request accepted",
                        otherProfile.getUsername() + " agreed to play chess with you. Start the game now?",
                        new String[]{"Sure", "Later"},
                        (option) -> {
                            if (option.equals("Sure")) {
                                Store.router.push("/fxml/game.fxml", userGame);
                            }
                        }
                );
                break;
            case "/topic/moves":
                Store.gameController.onSocketMove((SocketMove) payload);
                break;
            case "/topic/messages":
                Store.gameController.onSocketMessage((Message) payload);
                break;
        }
    }
}
