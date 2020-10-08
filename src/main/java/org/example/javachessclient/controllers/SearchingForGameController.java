package org.example.javachessclient.controllers;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
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
        PathTransition transition = new PathTransition();
        transition.setPath(new Circle(20, 0, 20));
        transition.setDuration(Duration.millis(3000));
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setCycleCount(Timeline.INDEFINITE);
        transition.setNode(searchImage);
        transition.play();
    }

    @FXML
    void onCancelSearch() {
        GameService.cancelSearchForRandomGame();
        Store.router.pop();
    }
}
