package org.burrow_studios.obelisk.server.net.http;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record Request(
        @NotNull APIHandler handler,
        @NotNull Endpoint endpoint,
        @NotNull Map<String, String> headers
) { }
