package org.burrow_studios.obelisk.core.source;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Response {
    private final @NotNull DataProvider provider;
    private final long id;

    private final int code; // TODO: remove?
    private final @Nullable JsonElement content;

    public Response(
            @NotNull DataProvider provider,
            long id,
            int code,
            @Nullable JsonElement content
    ) {
        this.provider = provider;
        this.id = id;

        this.code = code;
        this.content = content;
    }

    public @NotNull DataProvider getProvider() {
        return provider;
    }

    public long getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    public @Nullable JsonElement getContent() {
        return content;
    }
}
