package org.burrow_studios.obelisk.commons.http.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public record Response(
        int code,
        @NotNull Map<String, String> headers,
        @Nullable String body
) {
    public boolean hasBody() {
        return body != null && !body.isEmpty();
    }

    public int bodyLength() {
        if (body != null && !body.isEmpty())
            return body.length();
        return -1;
    }

    public byte[] bodyBytes() {
        if (body == null)
            return new byte[0];
        return body.getBytes();
    }
}
