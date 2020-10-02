package org.example.javachessclient.socketgame;

import org.example.javachessclient.controllers.GameController;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class SocketGameService {
    private static final String socketServerUrl = "http://localhost:8081/api/socket/game/";

    public static StompSession createStompSession() {
        try {
            WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            return stompClient.connect(socketServerUrl, new GameStompSessionHandler()).get();
        } catch (InterruptedException | ExecutionException exception) {
            System.out.println("Failed to connect to socket server: " + exception.getMessage());
            return null;
        }
    }
}
