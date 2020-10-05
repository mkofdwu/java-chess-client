package org.example.javachessclient.services;

import org.example.javachessclient.Store;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SocketGameService {
    public static final String socketServerUrl = "ws://localhost:8081/api/ws/game";

    public static StompSession createStompSession() {
        try {
            List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
            WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            headers.add("Authorization", "Bearer " + Store.token);
            return stompClient.connect(socketServerUrl, new WebSocketHttpHeaders(headers), new GameStompSessionHandler()).get();
        } catch (InterruptedException | ExecutionException exception) {
            System.out.println("Failed to connect to socket server: " + exception.getMessage());
            return null;
        }
    }
}
