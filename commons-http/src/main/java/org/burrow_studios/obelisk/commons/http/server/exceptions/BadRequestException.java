package org.burrow_studios.obelisk.commons.http.server.exceptions;

import org.burrow_studios.obelisk.commons.http.server.Response;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;

public class BadRequestException extends RequestHandlerException {
    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public Response asResponse() {
        return new ResponseBuilder()
                .setSimpleBody(getMessage())
                .setCode(400)
                .build();
    }
}
