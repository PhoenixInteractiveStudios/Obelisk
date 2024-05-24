package org.burrow_studios.obelisk.monolith.http.exceptions;

import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

public class BadRequestException extends RequestHandlerException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public @NotNull Response toResponse() {
        return Response.error(400, "Bad Request", this.getMessage());
    }
}
