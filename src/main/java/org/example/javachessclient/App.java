package org.example.javachessclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.example.javachessclient.apis.AuthApi;
import org.example.javachessclient.apis.FileApi;
import org.example.javachessclient.apis.GameApi;
import org.example.javachessclient.apis.UserApi;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.services.AuthService;
import org.example.javachessclient.services.ThemeService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.ParseException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        root.getChildren().add(new Pane()); // the first, bottommost child will be controlled by Router

        Store.router = new Router(root, 0);
        Store.modal = new Modal(root);
        Store.root = root;

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(chain -> {
            if (Store.token != null) {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Store.token).build();
                return chain.proceed(request);
            }
            return chain.proceed(chain.request());
        });
        Store.retrofit = new Retrofit.Builder()
                .baseUrl(
                        System.getProperty("production").equals("true")
                                ? "https://java-chess-server.herokuapp.com/api/"
                                : "http://localhost:8081/api/"
                )
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        Store.authApi = Store.retrofit.create(AuthApi.class);
        Store.userApi = Store.retrofit.create(UserApi.class);
        Store.gameApi = Store.retrofit.create(GameApi.class);
        Store.fileApi = Store.retrofit.create(FileApi.class);

        ThemeService.setTheme(0);
        ThemeService.setAccent(1);

        Store.router.push("/fxml/splash.fxml");

        new Thread(() -> {
            // here all the heavy startup tasks are run (currently only authentication)
            boolean authenticated = AuthService.attemptAuthenticateFromFile();
            Platform.runLater(() -> {
                if (authenticated) {
                    ThemeService.setTheme(Store.user.getSettings().getTheme());
                    ThemeService.setAccent(Store.user.getSettings().getAccent());
                }
                Store.router.push(authenticated ? "/fxml/layout.fxml" : "/fxml/landing.fxml");
            });
        }).start();

        Scene scene = new Scene(root);
        stage.setTitle("JavaChess");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void startTestChess(Stage stage) {
        StackPane root = new StackPane();
        try {
            Chess chess = new Chess();
            chess.loadFEN(Chess.startingFen);
            chess.setCanPlayWhite(true);
            chess.setCanPlayBlack(true);
            root.getChildren().add(chess.getCanvas());
            Store.modal = new Modal(root);

            Scene scene = new Scene(root);
            stage.setTitle("JavaChess");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (ParseException e) {
            //
        }
    }

    public static void main(String[] args) {
        launch();
    }
}