module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.javachessclient to javafx.fxml;
    exports org.example.javachessclient;
}