package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public final class RPCRequest {
    private final long id;
    private final @NotNull Instant time;
    private final @NotNull String path;
    private final @NotNull Method method;
    private final @NotNull JsonObject headers;
    private final @Nullable JsonElement body;

    private final @NotNull CompletableFuture<RPCResponse> future;

    private final @NotNull JsonObject json;

    public RPCRequest(long id, @NotNull Instant time, @NotNull Method method, @NotNull String path, @NotNull JsonObject headers, @Nullable JsonElement body) {
        this.id = id;
        this.time = time;
        this.path = path;
        this.method = method;
        this.headers = headers.deepCopy();

        this.body = body == null ? null : body.deepCopy();

        this.json = new JsonObject();
        this.json.addProperty("id", this.id);
        this.json.addProperty("time", time.toString());
        this.json.addProperty("method", this.method.name());
        this.json.addProperty("path", this.path);
        this.json.add("headers", this.headers);
        if (this.body != null)
            this.json.add("body", this.body);

        this.future = new CompletableFuture<>();
    }

    public long getId() {
        return this.id;
    }

    public @NotNull Instant getTime() {
        return this.time;
    }

    public long getMillis() {
        return this.time.toEpochMilli();
    }

    public @NotNull Method getMethod() {
        return this.method;
    }

    public @NotNull String getPath() {
        return this.path;
    }

    public @NotNull JsonObject getHeaders() {
        return this.headers.deepCopy();
    }

    public @Nullable JsonElement getBody() {
        if (this.body == null)
            return null;
        return this.body.deepCopy();
    }

    public @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }

    public @NotNull CompletableFuture<RPCResponse> getFuture() {
        return this.future;
    }

    /** A helper class to create {@link RPCRequest Requests}. As opposed to the resulting Request, this class is mutable. */
    public static final class Builder extends RPCExchangeBuilder<Builder> {
        private Method method;
        private String path;

        public Builder() { }

        public Method getMethod() {
            return method;
        }

        public Builder setMethod(@NotNull Method method) {
            this.method = method;
            return this;
        }

        public String getPath() {
            return path;
        }

        public Builder setPath(@NotNull String path) {
            this.path = path;
            return this;
        }

        public @NotNull RPCRequest build(long id) {
            if (method == null)
                throw new IllegalArgumentException("Method must be set");
            if (path == null)
                throw new IllegalArgumentException("Path must be set");

            return new RPCRequest(id, Instant.now(), method, path, headers, body);
        }
    }
}
