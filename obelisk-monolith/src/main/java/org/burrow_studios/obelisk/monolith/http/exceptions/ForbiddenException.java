package org.burrow_studios.obelisk.monolith.http.exceptions;

import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

public class ForbiddenException extends RequestHandlerException {
    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public @NotNull Response toResponse() {
        return Response.error(403, "Forbidden",  this.getMessage());
    }
}
