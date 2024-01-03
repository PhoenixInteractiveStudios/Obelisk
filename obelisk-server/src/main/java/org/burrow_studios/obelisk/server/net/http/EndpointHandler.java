package org.burrow_studios.obelisk.server.net.http;

import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EndpointHandler {
    void handle(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException;
}
