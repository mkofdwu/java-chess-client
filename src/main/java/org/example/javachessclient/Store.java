package org.example.javachessclient;

import org.example.javachessclient.controllers.GameController;
import org.example.javachessclient.models.User;
import org.springframework.messaging.simp.stomp.StompSession;
import retrofit2.Retrofit;

public class Store {
    public static String token;
    public static User user;
    public static Router router;
    public static Modal modal;

    public static Retrofit retrofit;
    public static StompSession stompSession;
    public static GameController gameController;
}
