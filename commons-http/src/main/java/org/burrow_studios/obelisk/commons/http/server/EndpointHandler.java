package org.burrow_studios.obelisk.commons.http.server;

import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EndpointHandler {
    void handle(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException;
}
