package org.example.javachessclient.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.views.PromotionOptionsModal;

public class AboutModal {
    public static VBox buildModal() {
        VBox modal = new VBox();
        modal.getStylesheets().add(PromotionOptionsModal.class.getResource("/styles/main.css").toExternalForm());
        modal.setPrefWidth(500);
        modal.setPadding(new Insets(34, 40, 34, 40));
        modal.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");

        Label titleLabel = new Label("About JavaChess");
        titleLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label byLabel = new Label("by Jia Jie");
        byLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 14px; -fx-text-fill: -fx-less;");

        Separator separator = new Separator();
        separator.setStyle("-fx-max-width: 100px; -fx-background-color: -fx-less;");

        Label bodyLabel = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean enim pulvinar morbi at lobortis. Neque, ultricies non vulputate nam non duis sem. Morbi amet dignissim fermentum purus. Quisque a morbi mauris non id ut adipiscing. Amet risus morbi nisi id purus viverra. Pretium lobortis turpis vel aliquet mattis in tortor pretium. Nam eget diam lacinia nullam.\n" +
                "\n" +
                "Pharetra ut eget aliquam et amet, amet at. Ultrices ultricies lobortis blandit mi. Hendrerit amet pretium sed urna nam viverra sed nullam consequat. Euismod at nullam interdum faucibus pellentesque nam felis at eu.");
        bodyLabel.setWrapText(true);

        HBox optionsBox = new HBox();
        HBox langBtn = new HBox();
        langBtn.setStyle("-fx-padding: 8px 12px 8px 12px; -fx-spacing: 10px; -fx-background-color: -fx-accent; -fx-background-radius: 5px;");
        Label selectedLangLabel = new Label("EN");
        selectedLangLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 14px; -fx-text-fill: white; -fx-label-padding: 0;");
        langBtn.getChildren().addAll(
                new ImageView(AboutModal.class.getResource("/icons/international.png").toExternalForm()),
                selectedLangLabel
        );
        langBtn.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: -fx-accent, -fx-absolute;" +
                "-fx-background-insets: 0 -1 -1 -1, 0 -1 0 -1;" +
                "-fx-background-radius: 0;" +
                "-fx-font-size: 16px;" +
                "-fx-text-fill: -fx-accent;" +
                "-fx-padding: 0;" +
                "-fx-cursor: hand;");
        closeBtn.setOnMouseClicked((e) -> Store.modal.hide());
        HBox.setHgrow(spacer, Priority.ALWAYS);
        optionsBox.getChildren().addAll(langBtn, spacer, closeBtn);
        optionsBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox.setMargin(titleLabel, new Insets(0, 0, 6, 0));
        VBox.setMargin(byLabel, new Insets(0, 0, 16, 0));
        VBox.setMargin(separator, new Insets(0, 0, 36, 0));
        VBox.setMargin(bodyLabel, new Insets(0, 0, 36, 0));
        modal.getChildren().addAll(titleLabel, byLabel, separator, bodyLabel, optionsBox);
        return modal;
    }
}
