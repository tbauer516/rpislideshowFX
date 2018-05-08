module info.tylerbauer.rpislideshow {
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.ws.rs;
    requires jackson.annotations;
    requires google.api.client;
    requires google.oauth.client;
    requires google.oauth.client.java6;
    requires google.oauth.client.jetty;
    requires google.http.client;
    requires google.http.client.jackson2;
    requires google.api.services.drive.v3.rev110;

    exports info.tylerbauer.rpislideshow;
    opens info.tylerbauer.rpislideshow.controllers;
}