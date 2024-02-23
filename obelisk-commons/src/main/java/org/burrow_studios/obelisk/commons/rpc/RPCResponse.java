package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RPCResponse {
    private final @NotNull RPCRequest request;
    private final @NotNull Instant time;
    private final @NotNull JsonObject headers;
    private final @NotNull Status status;
    private final @Nullable JsonElement body;

    private final @NotNull JsonObject json;

    public RPCResponse(@NotNull RPCRequest request, @NotNull Instant time, @NotNull JsonObject headers, @NotNull Status status, @Nullable JsonElement body) {
        this.request = request;
        this.time = time;
        this.headers = headers.deepCopy();
        this.status = status;

        this.body = body == null ? null : body.deepCopy();

        this.json = new JsonObject();
        this.json.addProperty("id", request.getId());
        this.json.addProperty("time", time.toString());
        this.json.addProperty("status", status.name());
        this.json.add("headers", this.headers);
        if (this.body != null)
            this.json.add("body", this.body);

        this.request.getFuture().complete(this);
    }

    public @NotNull RPCRequest getRequest() {
        return this.request;
    }

    public @NotNull Instant getTime() {
        return this.time;
    }

    public long getMillis() {
        return this.time.toEpochMilli();
    }

    public @NotNull JsonObject getHeaders() {
        return this.headers.deepCopy();
    }

    public @NotNull Map<String, String> getHeaderMap() {
        Map<String, String> map = new LinkedHashMap<>();
        JsonObject headers = this.getHeaders();
        for (String name : headers.keySet()) {
            JsonElement value = headers.get(name);

            if (value instanceof JsonArray arr) {
                List<String> values = new ArrayList<>(arr.size());
                for (JsonElement element : arr)
                    values.add(element.getAsString());
                map.put(name, String.join(", ", values));
            } else {
                map.put(name, value.getAsString());
            }
        }
        return Map.copyOf(map);
    }

    public @NotNull Status getStatus() {
        return this.status;
    }

    public @Nullable JsonElement getBody() {
        if (this.body == null)
            return null;
        return this.body.deepCopy();
    }

    public @Nullable String getBodyString() {
        if (this.body == null)
            return null;
        if (this.body instanceof JsonNull)
            return "null";
        return RPCServer.GSON.toJson(body);
    }

    public @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }

    /** A helper class to create {@link RPCResponse Responses}. As opposed to the resulting Response, this class is mutable. */
    public static final class Builder extends RPCExchangeBuilder<Builder> {
        private final @NotNull RPCRequest request;
        private Status status;

        public Builder(@NotNull RPCRequest request) {
            this.request = request;
        }

        public Builder setStatus(@NotNull Status status) {
            this.status = status;
            return this;
        }

        public @NotNull RPCResponse build() throws IllegalArgumentException {
            if (status == null)
                throw new IllegalArgumentException("Status must be set");

            return new RPCResponse(request, Instant.now(), headers, status, body);
        }
    }
}
