package org.example.javachessclient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.example.javachessclient.controllers.Controller;
import org.example.javachessclient.controllers.GameController;

import java.io.IOException;
import java.util.ArrayList;

public class Router {
    private final Pane root;
    private final int childIndex;
    private final ArrayList<Parent> history;

    public Router(Pane root, int childIndex) {
        this.root = root;
        this.childIndex = childIndex;
        history = new ArrayList<>();
    }

    public void push(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            root.getChildren().set(childIndex, page);
            history.add(page);
        } catch (IOException exception) {
            System.out.println("Failed to load fxml: " + exception.getMessage());
        }
    }

    public void push(String fxmlPath, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            ((Controller) loader.getController()).loadData(data);
            root.getChildren().set(childIndex, page);
            history.add(page);
        } catch (IOException exception) {
            System.out.println("Failed to load fxml: " + exception.getMessage());
        }
    }

    public void replace(String fxmlPath) {
        history.remove(history.size() - 1);
        push(fxmlPath);
    }

    public void pop() {
        history.remove(history.size() - 1);
        Parent page = history.get(history.size() - 1);
        root.getChildren().set(childIndex, page);
    }

    public void clearAndPush(String fxmlPath) {
        history.clear();
        push(fxmlPath);
    }
}
