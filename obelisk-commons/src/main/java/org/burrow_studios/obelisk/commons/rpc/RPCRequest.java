package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public final class RPCRequest extends RPCExchange {
    private final long id;
    private final @NotNull String path;
    private final @NotNull Method method;
    private final @NotNull TimeoutContext timeout;

    private final @NotNull CompletableFuture<RPCResponse> future;

    public RPCRequest(long id, @NotNull Instant time, @NotNull Method method, @NotNull String path, @NotNull TimeoutContext timeout, @NotNull JsonObject headers, @Nullable JsonElement body) {
        super(time, headers, body);
        this.id = id;
        this.path = path;
        this.method = method;
        this.timeout = timeout;

        this.json.addProperty("timeout", timeout.asInstant().toString());
        this.json.addProperty("method", this.method.name());
        this.json.addProperty("path", this.path);

        this.future = new CompletableFuture<>();
    }

    @Override
    public long getId() {
        return this.id;
    }

    public @NotNull Method getMethod() {
        return this.method;
    }

    public @NotNull String getPath() {
        return this.path;
    }

    public @NotNull TimeoutContext getTimeout() {
        return timeout;
    }

    public @NotNull CompletableFuture<RPCResponse> getFuture() {
        return this.future;
    }

    /** A helper class to create {@link RPCRequest Requests}. As opposed to the resulting Request, this class is mutable. */
    public static final class Builder extends RPCExchangeBuilder<Builder> {
        private Method method;
        private String path;
        private TimeoutContext timeout;

        public Builder() { }

        public Method getMethod() {
            return method;
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public String getPath() {
            return path;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public TimeoutContext getTimeout() {
            return this.timeout;
        }

        public Builder setTimeout(TimeoutContext timeout) {
            this.timeout = timeout;
            return this;
        }

        public @NotNull RPCRequest build(long id) {
            if (method == null)
                throw new IllegalArgumentException("Method must be set");
            if (path == null)
                throw new IllegalArgumentException("Path must be set");
            if (time == null)
                time = Instant.now();
            if (timeout == null)
                timeout = TimeoutContext.DEFAULT;

            return new RPCRequest(id, time, method, path, timeout, headers, body);
        }
    }
}
