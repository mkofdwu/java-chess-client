package org.example.javachessclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.javachessclient.chess.Chess;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
        Chess chess = new Chess(); // testing
        Parent root = new Pane(chess.getCanvas());
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