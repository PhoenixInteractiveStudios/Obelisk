package org.burrow_studios.obelisk.internal.net.http;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public record CompiledRoute(
        @NotNull Route route,
        @NotNull String uri
) {
    public @NotNull Method method() {
        return route().getMethod();
    }

    public @NotNull URI asURI() {
        return URI.create(uri);
    }
}
