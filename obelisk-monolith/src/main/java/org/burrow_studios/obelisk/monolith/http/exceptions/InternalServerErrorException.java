package org.burrow_studios.obelisk.monolith.http.exceptions;

import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

public class InternalServerErrorException extends RequestHandlerException {
    public InternalServerErrorException() {
        super();
    }

    @Override
    public @NotNull Response toResponse() {
        return Response.error(500, "Internal Server Error");
    }
}
