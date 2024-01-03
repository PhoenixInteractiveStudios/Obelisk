package org.burrow_studios.obelisk.server.net.http;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseBuilder {
    private int code;
    private final ConcurrentHashMap<String, String> headers;
    private String body;

    public ResponseBuilder() {
        this.headers = new ConcurrentHashMap<>();
        this.clear();
    }

    public void clear() {
        this.code = -1;
        this.headers.clear();
        this.body = null;
    }

    public @NotNull Response build() throws IllegalArgumentException {
        if (code == -1)
            throw new IllegalArgumentException("Missing status code");

        return new Response(
                code,
                Map.copyOf(headers),
                body
        );
    }

    /* - - - */

    public @NotNull ResponseBuilder setCode(int code) {
        this.code = code;
        return this;
    }

    public @NotNull ResponseBuilder setHeader(@NotNull String field, @NotNull String value) {
        this.headers.put(field, value);
        return this;
    }

    public @NotNull ResponseBuilder setBody(@NotNull String body) {
        this.body = body;
        return this;
    }

    public @NotNull ResponseBuilder setContentType(@NotNull String contentType) {
        return this.setHeader("Content-Type", contentType);
    }
}
