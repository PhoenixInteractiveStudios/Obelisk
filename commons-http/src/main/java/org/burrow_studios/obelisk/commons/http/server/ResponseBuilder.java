package org.burrow_studios.obelisk.commons.http.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** A helper class to create {@link Response Responses}. As opposed to the resulting Response, this class is mutable. */
public class ResponseBuilder {
    private int code;
    private final ConcurrentHashMap<String, String> headers;
    private String body;

    public ResponseBuilder() {
        this.headers = new ConcurrentHashMap<>();
        this.clear();
    }

    /** Resets this instance and clears all previously set attributes. */
    public void clear() {
        this.code = -1;
        this.headers.clear();
        this.body = null;
    }

    /**
     * Creates a result {@link Response} this builder was configured to create.
     * @throws IllegalArgumentException if no status code has been specified yet.
     * @see #setCode(int)
     */
    public @NotNull Response build() throws IllegalArgumentException {
        if (code == -1)
            throw new IllegalArgumentException("Missing status code");

        return new Response(
                code,
                Map.copyOf(headers),
                body
        );
    }

    /* - - - */

    /**
     * Specifies the HTTP status code of the response.
     * @return This ResponseBuilder for chaining purposes.
     */
    public @NotNull ResponseBuilder setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * Specifies a single response header. If a header with the same {@code field} has already been set, it will be overwritten.
     * @return This ResponseBuilder for chaining purposes.
     */
    public @NotNull ResponseBuilder setHeader(@NotNull String field, @NotNull String value) {
        this.headers.put(field, value);
        return this;
    }

    /**
     * Specifies the response body.
     * @return This ResponseBuilder for chaining purposes.
     */
    public @NotNull ResponseBuilder setBody(@NotNull String body) {
        this.body = body;
        return this;
    }

    /**
     * Specifies the response body.
     * @return This ResponseBuilder for chaining purposes.
     */
    public @NotNull ResponseBuilder setBody(@NotNull JsonElement json) {
        return this.setBody(HTTPServer.GSON.toJson(json))
                .setContentType("application/json");
    }

    /**
     * Specifies the response body as a simple JSON object with only one element with the name "message" and the
     * provided {@code message} value.
     * @return This ResponseBuilder for chaining purposes.
     */
    public @NotNull ResponseBuilder setSimpleBody(@NotNull String message) {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);
        return this.setBody(json);
    }

    /**
     * Specifies the response content type.
     * This would be equivalent to calling {@link #setHeader(String, String)} with the {@code field}
     * "{@code Content-Type}" and the same {@code value} as {@code contentType}.
     * @return This ResponseBuilder for chaining purposes.
     */
    public @NotNull ResponseBuilder setContentType(@NotNull String contentType) {
        return this.setHeader("Content-Type", contentType);
    }
}
