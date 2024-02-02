package org.burrow_studios.obelisk.commons.http.server.exceptions;

import org.burrow_studios.obelisk.commons.http.server.Response;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;

public class UnauthorizedException extends RequestHandlerException {
    @Override
    public Response asResponse() {
        return new ResponseBuilder()
                .setCode(401)
                .setHeader("WWW-Authenticate", "Bearer")
                .build();
    }
}
