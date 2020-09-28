module org.example.javachessclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires spring.websocket;
    requires spring.messaging;

    opens org.example.javachessclient to javafx.fxml;
    exports org.example.javachessclient;
    opens org.example.javachessclient.controllers to javafx.fxml;
}