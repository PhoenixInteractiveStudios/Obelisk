package org.burrow_studios.obelisk.commons.rpc;

import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EndpointHandler {
    void handle(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException;
}
