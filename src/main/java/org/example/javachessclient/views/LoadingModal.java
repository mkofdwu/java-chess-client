package org.example.javachessclient.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import org.example.javachessclient.chess.views.PromotionOptionsModal;

public class LoadingModal {
    public static VBox buildModal() {
        VBox modal = new VBox();
        modal.getStylesheets().add(PromotionOptionsModal.class.getResource("/styles/main.css").toExternalForm());
        modal.setAlignment(Pos.CENTER);
        ProgressIndicator loader = new ProgressIndicator();
        loader.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        loader.setStyle("-fx-progress-color: white;");
        VBox.setMargin(loader, new Insets(0, 0, 20, 0));
        Label label = new Label("Loading ...");
        label.setStyle("-fx-text-fill: white;");
        modal.getChildren().addAll(loader, label);
        return modal;
    }
}
