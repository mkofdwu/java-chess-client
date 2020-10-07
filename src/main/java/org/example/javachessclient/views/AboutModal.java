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

import java.util.Locale;
import java.util.ResourceBundle;

public class AboutModal {
    private static final String[] langs = new String[]{"en-US", "zh-CN", "fr-FR"};

    private VBox modal;
    private HBox optionsBox;
    private HBox selectLangBtn;
    private HBox availableLangsBox;

    private String selectedLang;
    private ResourceBundle rb = ResourceBundle.getBundle("AboutModal", Locale.getDefault());

    public AboutModal() {
        modal = new VBox();
        modal.getStylesheets().add(PromotionOptionsModal.class.getResource("/styles/main.css").toExternalForm());
        modal.setPrefWidth(500);
        modal.setPadding(new Insets(34, 40, 34, 40));
        modal.setStyle("-fx-background-color: -fx-absolute; -fx-background-radius: 5px;");

        Label titleLabel = new Label(rb.getString("title"));
        titleLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label byLabel = new Label(rb.getString("byLine"));
        byLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 14px; -fx-text-fill: -fx-less;");

        Separator separator = new Separator();
        separator.setStyle("-fx-max-width: 100px; -fx-background-color: -fx-less;");

        Label bodyLabel = new Label(rb.getString("body"));
        bodyLabel.setWrapText(true);

        optionsBox = new HBox();
        selectLangBtn = new HBox();
        selectLangBtn.setStyle("-fx-padding: 8px 12px 8px 12px; -fx-spacing: 10px; -fx-background-color: -fx-accent; -fx-background-radius: 5px;");
        Label selectedLangLabel = new Label("EN");
        selectedLangLabel.setStyle("-fx-font-family: 'Domaine Display Test'; -fx-font-size: 14px; -fx-text-fill: white; -fx-label-padding: 0;");
        selectLangBtn.getChildren().addAll(
                new ImageView(AboutModal.class.getResource("/icons/international.png").toExternalForm()),
                selectedLangLabel
        );
        selectLangBtn.setAlignment(Pos.CENTER);
        selectLangBtn.setOnMouseClicked((e) -> showAvailableLangs());
        availableLangsBox = new HBox();
        availableLangsBox.setStyle("-fx-background-radius: 5px");
        for (String lang : langs) {
            Button optionBtn = new Button(lang);
            optionBtn.setStyle("-fx-background-radius: 0; -fx-background-color: red");
            optionBtn.setOnMouseClicked((e) -> selectLang(lang));
            availableLangsBox.getChildren().add(optionBtn);
        }
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
        optionsBox.getChildren().addAll(selectLangBtn, spacer, closeBtn);
        optionsBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox.setMargin(titleLabel, new Insets(0, 0, 6, 0));
        VBox.setMargin(byLabel, new Insets(0, 0, 16, 0));
        VBox.setMargin(separator, new Insets(0, 0, 36, 0));
        VBox.setMargin(bodyLabel, new Insets(0, 0, 36, 0));
        modal.getChildren().addAll(titleLabel, byLabel, separator, bodyLabel, optionsBox);
    }

    private void showAvailableLangs() {
        optionsBox.getChildren().remove(0);
        optionsBox.getChildren().add(0, availableLangsBox);
    }

    private void selectLang(String lang) {
        selectedLang = lang;
        String[] langAndCountry = lang.split("-");
        rb = ResourceBundle.getBundle("AboutModal", new Locale(langAndCountry[0], langAndCountry[1]));

        optionsBox.getChildren().remove(0);
        optionsBox.getChildren().add(0, selectLangBtn);
    }

    public VBox getModal() {
        return modal;
    }
}
