package org.example.javachessclient.views;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LoadingModal {
    public static VBox buildModal() {
        VBox modal = new VBox();
//        Circle circle = new Circle(20, Color.RED);
//        ScaleTransition transition = new ScaleTransition();
//        transition.setByX();
        modal.getChildren().add(new Label("Loading ..."));
        return modal;
    }
}
