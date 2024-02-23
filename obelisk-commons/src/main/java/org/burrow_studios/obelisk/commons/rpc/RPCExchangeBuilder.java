package org.burrow_studios.obelisk.commons.rpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unchecked")
abstract sealed class RPCExchangeBuilder<T extends RPCExchangeBuilder<T>> permits RPCRequest.Builder, RPCResponse.Builder {
    protected final @NotNull JsonObject headers = new JsonObject();
    protected @Nullable JsonElement body;

    protected RPCExchangeBuilder() { }

    public final T setHeaders(@NotNull String key, @NotNull JsonArray val) {
        for (JsonElement element : val)
            this.addHeader(key, element.getAsString());
        return (T) this;
    }

    public final T setHeaders(@NotNull String key, @NotNull List<String> val) {
        for (String str : val)
            this.addHeader(key, str);
        return (T) this;
    }

    public final T setHeader(@NotNull String key, @NotNull String val) {
        headers.addProperty(key, val);
        return (T) this;
    }

    public final T addHeader(@NotNull String key, @NotNull String val) {
        JsonElement currentHeader = headers.get(key);

        if (currentHeader instanceof JsonArray currentArray) {
            currentArray.add(val);
            return (T) this;
        }

        JsonArray arr = new JsonArray();
        if (currentHeader != null)
            arr.add(currentHeader);
        arr.add(val);
        headers.add(key, arr);

        return (T) this;
    }

    public T setBody(byte[] body) {
        if (body.length == 0) {
            this.body = null;
            return (T) this;
        }
        return this.setBody(new String(body));
    }

    public T setBody(@Nullable String body) {
        if (body == null) {
            this.body = null;
            return (T) this;
        }
        if (body.isEmpty()) {
            this.body = JsonNull.INSTANCE;
            return (T) this;
        }
        return this.setBody(RPCServer.GSON.fromJson(body, JsonElement.class));
    }

    public T setBody(@Nullable JsonElement body) {
        this.body = body;
        return (T) this;
    }
}
