package org.example.javachessclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.example.javachessclient.services.AuthService;
import org.example.javachessclient.socketgame.SocketGameService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.getChildren().add(new Pane());

        Store.router = new Router(root, 0);
        Store.modal = new Modal(root);
        boolean authenticated = AuthService.attemptAuthenticateFromFile();
        Store.router.push(authenticated ? "/fxml/layout.fxml" : "/fxml/landing.fxml");

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", Store.token).build();
            return chain.proceed(request);
        });
        Store.retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081/api/game/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        Store.stompSession = SocketGameService.createStompSession();

        Scene scene = new Scene(root);
        stage.setTitle("JavaChess");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}