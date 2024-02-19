package org.burrow_studios.obelisk.commons.http;

import org.burrow_studios.obelisk.commons.http.client.HTTPClient;
import org.burrow_studios.obelisk.commons.http.server.HTTPServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * An immutable representation of an HTTP response. In the context of an {@link HTTPClient} this represents the response
 * from the remote server. In the context of an {@link HTTPServer} this represents the response that is sent to the
 * remote client.
 * @param code The HTTP status code.
 * @param headers The HTTP response headers.
 * @param body The HTTP response body (may be null).
 */
public record HTTPResponse(
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
