package org.example.javachessclient;

import javafx.scene.Parent;
import org.example.javachessclient.apis.AuthApi;
import org.example.javachessclient.apis.FileApi;
import org.example.javachessclient.apis.GameApi;
import org.example.javachessclient.apis.UserApi;
import org.example.javachessclient.controllers.GameController;
import org.example.javachessclient.models.User;
import org.springframework.messaging.simp.stomp.StompSession;
import retrofit2.Retrofit;

public class Store {
    public static String token;
    public static User user;
    public static Router router;
    public static Modal modal;
    // used for changing styles
    public static Parent root;

    public static Retrofit retrofit;
    // this is a temporary solution. In the future, replace these with instances of AuthService, UserService, other Services, etc
    public static AuthApi authApi;
    public static UserApi userApi;
    public static GameApi gameApi;
    public static FileApi fileApi;
    public static StompSession stompSession;
    public static GameController gameController;
}
