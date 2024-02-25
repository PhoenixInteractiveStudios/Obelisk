package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RPCResponse extends RPCExchange {
    private final @NotNull RPCRequest request;
    private final @NotNull Status status;

    public RPCResponse(@NotNull RPCRequest request, @NotNull Instant time, @NotNull JsonObject headers, @NotNull Status status, @Nullable JsonElement body) {
        super(time, headers, body);

        this.request = request;
        this.status = status;

        this.json.addProperty("status", status.name());

        this.request.getFuture().complete(this);
    }

    @Override
    public long getId() {
        return this.request.getId();
    }

    public @NotNull RPCRequest getRequest() {
        return this.request;
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

    public @Nullable String getBodyString() {
        if (this.body == null)
            return null;
        if (this.body instanceof JsonNull)
            return "null";
        return RPCServer.GSON.toJson(body);
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
            if (time == null)
                time = Instant.now();

            return new RPCResponse(request, time, headers, status, body);
        }
    }
}
