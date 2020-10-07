package org.example.javachessclient.controllers;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.example.javachessclient.Store;
import org.example.javachessclient.services.GameService;

public class SearchingForGameController {
    @FXML
    private ImageView searchImage;

    public void initialize() {
//        Path path = new Path();
//        path.getElements().add(new MoveTo(500, 350));
//        path.getElements().add(new Rotate())
//        path.getElements().add(new QuadCurveTo(0, 0, 0, 0));
//        PathTransition transition = new PathTransition();
//        transition.setDuration(Duration.millis(6000));
//        transition.setPath(path);
//        transition.setNode(searchImage);
//        transition.play();
    }

    @FXML
    void onCancelSearch() {
        GameService.cancelSearchForRandomGame();
        Store.router.pop();
    }
}
