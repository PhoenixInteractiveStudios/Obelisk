package org.burrow_studios.obelisk.monolith.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.burrow_studios.obelisk.monolith.http.HTTPServer.GSON;

public class Response {
    private final int status;
    private final Map<String, String> headers;
    private final String body;

    private Response(
            int status,
            @NotNull Map<String, String> headers,
            @Nullable String body
    ) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public int getStatus() {
        return this.status;
    }

    public @NotNull Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    public @Nullable String getBody() {
        return this.body;
    }

    public static class Builder {
        private Integer status;
        private String body;

        private final Map<String, String> headers;

        public Builder() {
            this.headers = new LinkedHashMap<>();
        }

        public Response build() throws IllegalArgumentException {
            if (status == null)
                throw new IllegalArgumentException("Status must be set");

            return new Response(status, headers, body);
        }

        public Builder setStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setBody(JsonElement body) {
            this.body = GSON.toJson(body);
            this.setHeader("Content-Type", "application/json");
            return this;
        }

        public Builder setHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.compute(key, (s, current) -> {
                if (current == null)
                    return value;
                return current + ", " + value;
            });
            return this;
        }
    }

    public static Response error(int status, @NotNull String message) {
        return error(status, message, (JsonElement) null);
    }

    public static Response error(int status, @NotNull String message, @Nullable String details) {
        if (details == null)
            return error(status, message);
        return error(status, message, new JsonPrimitive(details));
    }

    public static Response error(int status, @NotNull String message, @Nullable JsonElement details) {
        return error(status, message, details, Map.of());
    }

    public static Response error(int status, @NotNull String message, @Nullable JsonElement details, @NotNull Map<String, String> headers) {
        JsonObject body = new JsonObject();
        body.addProperty("status", status);
        body.addProperty("message", message);
        if (details != null)
            body.add("details", details);
        Builder builder = new Builder()
                .setStatus(status)
                .setBody(body);
        headers.forEach(builder::addHeader);
        return builder.build();
    }
}
