package org.burrow_studios.obelisk.core.net.http;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public record CompiledEndpoint(
        @NotNull Endpoint endpoint,
        @NotNull String uri
) {
    public @NotNull Method method() {
        return endpoint().getMethod();
    }

    public @NotNull URI asURI() {
        return URI.create(uri);
    }
}
