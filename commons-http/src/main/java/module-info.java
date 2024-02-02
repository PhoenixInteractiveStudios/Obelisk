module obliesk.commons.http {
    requires org.jetbrains.annotations;
    requires obelisk.commons;
    requires jdk.httpserver;
    requires java.net.http;
    requires com.auth0.jwt;
    requires com.google.gson;

    exports org.burrow_studios.obelisk.commons.http;
    exports org.burrow_studios.obelisk.commons.http.server;
    exports org.burrow_studios.obelisk.commons.http.client;
}