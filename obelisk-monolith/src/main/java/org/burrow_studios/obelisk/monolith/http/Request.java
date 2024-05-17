package org.burrow_studios.obelisk.monolith.http;

import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import org.burrow_studios.obelisk.core.http.Path;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class Request {
    private final HttpExchange rawExchange;
    private final Route.Compiled route;
    private final JsonElement body;
    private final Map<String, String> headers;

    public Request(
            @NotNull HttpExchange rawExchange,
            @NotNull Route.Compiled route,
            @Nullable JsonElement body,
            @NotNull Map<String, String> headers
    ) {
        this.rawExchange = rawExchange;
        this.route = route;
        this.body = body;
        this.headers = headers;
    }

    public @NotNull Route.Compiled getRoute() {
        return this.route;
    }

    public @NotNull Path getPath() {
        return this.getRoute().getPath();
    }

    public @Nullable JsonElement getBody() {
        return this.body;
    }

    public @NotNull Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }
}
