package org.burrow_studios.obelisk.monolith.http;

import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RequestHandler {
    @NotNull Response handle(@NotNull Request request) throws RequestHandlerException;
}
