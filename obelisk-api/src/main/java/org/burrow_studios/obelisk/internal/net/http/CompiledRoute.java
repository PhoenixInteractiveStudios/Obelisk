package org.burrow_studios.obelisk.internal.net.http;

import org.jetbrains.annotations.NotNull;

public record CompiledRoute(
        @NotNull Route route,
        @NotNull String uri
) {
    public @NotNull Method method() {
        return route().getMethod();
    }
}
