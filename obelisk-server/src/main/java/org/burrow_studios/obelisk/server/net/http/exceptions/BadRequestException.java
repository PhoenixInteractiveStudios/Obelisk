package org.burrow_studios.obelisk.server.net.http.exceptions;

import org.burrow_studios.obelisk.server.net.http.Response;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;

public class BadRequestException extends APIException {
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
