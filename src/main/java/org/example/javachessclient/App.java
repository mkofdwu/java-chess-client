package org.example.javachessclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.javachessclient.chess.Chess;
import org.example.javachessclient.services.AuthService;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.getChildren().add(new Pane());

        Store.router = new Router(root, 0);
        Store.modal = new Modal(root);
        boolean authenticated = AuthService.attemptAuthenticateFromFile();
        Store.router.push(authenticated ? "/fxml/layout.fxml" : "/fxml/landing.fxml");

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