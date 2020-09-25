package org.example.javachessclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.javachessclient.chess.Chess;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//         Parent root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
//        Chess chess = new Chess(); // testing
//        Parent root = new Pane(chess.getCanvas());
        StackPane root = new StackPane();
        root.getChildren().add(new Chess().getCanvas());

        Store.modal = new Modal(root);

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