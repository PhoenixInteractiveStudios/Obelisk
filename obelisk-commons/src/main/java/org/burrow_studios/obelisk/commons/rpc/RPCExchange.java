package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public sealed abstract class RPCExchange permits RPCRequest, RPCResponse {
    protected final @NotNull Instant time;
    protected final @NotNull JsonObject headers;
    protected final @Nullable JsonElement body;

    protected final @NotNull JsonObject json;

    protected RPCExchange(@NotNull Instant time, @NotNull JsonObject headers, @Nullable JsonElement body) {
        this.time = time;
        this.headers = headers;
        this.body = body == null ? null : body.deepCopy();

        this.json = new JsonObject();
        this.json.addProperty("id", getId());
        this.json.addProperty("time", time.toString());
        this.json.add("headers", headers);
        if (this.body != null)
            this.json.add("body", body);
    }

    public abstract long getId();

    public final @NotNull Instant getTime() {
        return this.time;
    }

    public final long getMillis() {
        return this.time.toEpochMilli();
    }

    public final @NotNull JsonObject getHeaders() {
        return this.headers.deepCopy();
    }

    public final @Nullable JsonElement getBody() {
        if (this.body == null)
            return null;
        return this.body.deepCopy();
    }

    public final @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }
}
