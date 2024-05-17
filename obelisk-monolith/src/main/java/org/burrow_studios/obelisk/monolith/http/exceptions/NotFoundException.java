package org.burrow_studios.obelisk.monolith.http.exceptions;

import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

public class NotFoundException extends RequestHandlerException {
    public NotFoundException() { }

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public @NotNull Response toResponse() {
        return Response.error(404, "Not Found", this.getMessage());
    }
}
