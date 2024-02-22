module obelisk.commons {
    requires com.auth0.jwt;
    requires com.google.gson;
    requires java.logging;
    requires java.net.http;
    requires jdk.httpserver;
    requires org.jetbrains.annotations;

    exports org.burrow_studios.obelisk.commons.crypto;
    exports org.burrow_studios.obelisk.commons.function;
    exports org.burrow_studios.obelisk.commons.logging;
    exports org.burrow_studios.obelisk.commons.turtle;
    exports org.burrow_studios.obelisk.commons.util;
    exports org.burrow_studios.obelisk.commons.util.validation;

    exports org.burrow_studios.obelisk.commons.rpc;
    exports org.burrow_studios.obelisk.commons.rpc.http;
}