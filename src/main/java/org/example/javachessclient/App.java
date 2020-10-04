package org.example.javachessclient;

import javafx.application.Application;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.ParseException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
//        StackPane root = new StackPane();
//        root.getChildren().add(new Pane());
//
//        Store.router = new Router(root, 0);
//        Store.modal = new Modal(root);
//
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        client.addInterceptor(chain -> {
//            if (Store.token != null) {
//                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Store.token).build();
//                return chain.proceed(request);
//            }
//            return chain.proceed(chain.request());
//        });
//        Store.retrofit = new Retrofit.Builder()
//                .baseUrl("http://localhost:8081/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client.build())
//                .build();
//        Store.authApi = Store.retrofit.create(AuthApi.class);
//        Store.userApi = Store.retrofit.create(UserApi.class);
//        Store.gameApi = Store.retrofit.create(GameApi.class);
//        Store.fileApi = Store.retrofit.create(FileApi.class);
//
//        boolean authenticated = AuthService.attemptAuthenticateFromFile();
//        Store.router.push(authenticated ? "/fxml/layout.fxml" : "/fxml/landing.fxml");

        Chess chess = new Chess();
        try {
            chess.loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            chess.setCanPlayWhite(true);
            chess.setCanPlayBlack(true);
            Pane root = new Pane(chess.getCanvas());

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