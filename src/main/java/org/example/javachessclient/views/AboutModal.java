package org.example.javachessclient.views;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.javachessclient.Store;
import org.example.javachessclient.chess.views.PromotionOptionsModal;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

// fixme: future: create class to manage resource bundle (LocaleService) and do string binding instead
public class AboutModal {
    private static final List<String> langs = List.of("en-US", "zh-CN", "fr-FR");

    private final VBox modal;
    private final ChoiceBox<String> availableLangsBox;
    private final Label titleLabel;
    private final Label byLabel;
    private final Label bodyLabel;

    private ResourceBundle rb = ResourceBundle.getBundle("/internationalization/AboutModal", new Locale("en", "US"));

    public AboutModal() {
        modal = new VBox();
        modal.getStylesheets().add(PromotionOptionsModal.class.getResource("/styles/main.css").toExternalForm());
        modal.setPrefWidth(500);
        modal.setPadding(new Insets(34, 40, 34, 40));
        modal.setStyle("-fx-background-color: -fx-absolute; -fx-background-radius: 5px;");

        titleLabel = new Label(rb.getString("title"));
        titleLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 28px; -fx-font-weight: bold;");

        byLabel = new Label(rb.getString("byLine"));
        byLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 14px; -fx-text-fill: -fx-less;");

        Separator separator = new Separator();
        separator.setStyle("-fx-max-width: 100px; -fx-background-color: -fx-less;");

        bodyLabel = new Label(rb.getString("body"));
        bodyLabel.setWrapText(true);

        HBox optionsBox = new HBox();

        availableLangsBox = new ChoiceBox<>(FXCollections.observableList(langs));
        availableLangsBox.setValue("en-US");
        availableLangsBox.setOnAction((e) -> selectLang(availableLangsBox.getValue()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("underline-btn");
        closeBtn.setOnMouseClicked((e) -> Store.modal.hide());

        optionsBox.getChildren().addAll(availableLangsBox, spacer, closeBtn);
        optionsBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox.setMargin(titleLabel, new Insets(0, 0, 6, 0));
        VBox.setMargin(byLabel, new Insets(0, 0, 16, 0));
        VBox.setMargin(separator, new Insets(0, 0, 36, 0));
        VBox.setMargin(bodyLabel, new Insets(0, 0, 36, 0));
        modal.getChildren().addAll(titleLabel, byLabel, separator, bodyLabel, optionsBox);
    }

    private void selectLang(String lang) {
        String[] langAndCountry = lang.split("-");
        rb = ResourceBundle.getBundle("/internationalization/AboutModal", new Locale(langAndCountry[0], langAndCountry[1]));
        reload();
    }

    private void reload() {
        titleLabel.setText(rb.getString("title"));
        byLabel.setText(rb.getString("byLine"));
        bodyLabel.setText(rb.getString("body"));
    }

    public VBox getModal() {
        return modal;
    }
}
