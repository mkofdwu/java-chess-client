module org.example.javachessclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires spring.websocket;
    requires spring.messaging;

    opens org.example.javachessclient to javafx.fxml;
    exports org.example.javachessclient;
}