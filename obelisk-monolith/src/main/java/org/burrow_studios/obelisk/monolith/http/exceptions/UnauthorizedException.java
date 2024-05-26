package org.burrow_studios.obelisk.monolith.http.exceptions;

import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UnauthorizedException extends RequestHandlerException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public @NotNull Response toResponse() {
        return Response.error(401, "Unauthorized",  this.getMessage(), Map.of("WWW-Authenticate", "Bearer"));
    }
}
