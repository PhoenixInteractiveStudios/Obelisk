package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.EnvUtil;
import org.jetbrains.annotations.NotNull;

public class GatewayHandler {
    public static final String DEFAULT_HOST = "gateway.burrow-studios.org";
    public static final int    DEFAULT_PORT = 8081;

    private final @NotNull String host;
    private final int port;

    public GatewayHandler() {
        this.host = EnvUtil.getString("GATEWAY_HOST", DEFAULT_HOST);
        this.port = EnvUtil.getInt("GATEWAY_PORT", DEFAULT_PORT);
    }

    public @NotNull Response onGet(@NotNull Request request) throws RequestHandlerException {
        JsonObject json = new JsonObject();
        json.addProperty("host", this.host);
        json.addProperty("port", this.port);
        return new Response.Builder()
                .setStatus(200)
                .setBody(json)
                .build();
    }
}
