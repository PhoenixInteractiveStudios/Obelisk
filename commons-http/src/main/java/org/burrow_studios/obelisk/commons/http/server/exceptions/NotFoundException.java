package org.burrow_studios.obelisk.commons.http.server.exceptions;

import org.burrow_studios.obelisk.commons.http.server.Response;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;

public class NotFoundException extends RequestHandlerException {
    @Override
    public Response asResponse() {
        return new ResponseBuilder()
                .setCode(404)
                .build();
    }
}
