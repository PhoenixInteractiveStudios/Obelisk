module obelisk.commons {
    requires org.jetbrains.annotations;
    requires java.logging;
    requires com.google.gson;
    requires com.auth0.jwt;
    requires jdk.httpserver;
    requires java.net.http;

    exports org.burrow_studios.obelisk.commons.crypto;
    exports org.burrow_studios.obelisk.commons.function;
    exports org.burrow_studios.obelisk.commons.logging;
    exports org.burrow_studios.obelisk.commons.turtle;
    exports org.burrow_studios.obelisk.commons.util;
    exports org.burrow_studios.obelisk.commons.util.validation;

    exports org.burrow_studios.obelisk.commons.http;
    exports org.burrow_studios.obelisk.commons.http.client;
    exports org.burrow_studios.obelisk.commons.http.server;
}