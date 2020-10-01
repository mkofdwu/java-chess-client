package org.example.javachessclient.socketgame;

import org.example.javachessclient.controllers.GameController;
import org.example.javachessclient.socketgame.models.Message;
import org.example.javachessclient.socketgame.models.Move;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.util.Objects;

public class GameStompSessionHandler implements StompSessionHandler {
    private final GameController gameController;

    public GameStompSessionHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        stompSession.subscribe("/topic/new", this);
        stompSession.subscribe("/topic/moves", this);
        stompSession.subscribe("/topic/messages", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        gameController.onSocketError(throwable);
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        gameController.onSocketError(throwable);
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        switch (Objects.requireNonNull(stompHeaders.getSubscription())) { // autosuggested
            case "/topic/new":
                return String.class;
            case "/topic/moves":
                return Move.class;
            case "/topic/messages":
                return Message.class;
        }
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        switch (Objects.requireNonNull(stompHeaders.getSubscription())) { // autosuggested
            case "/topic/new":
                gameController.onSocketNewGame((String) payload);
                break;
            case "/topic/moves":
                gameController.onSocketMove((Move) payload);
                break;
            case "/topic/messages":
                gameController.onSocketMessage((Message) payload);
                break;
        }
    }
}
