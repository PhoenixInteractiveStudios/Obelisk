package org.burrow_studios.obelisk.commons.http.server.exceptions;

import org.burrow_studios.obelisk.commons.http.HTTPResponse;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;

public class UnauthorizedException extends RequestHandlerException {
    @Override
    public HTTPResponse asResponse() {
        return new ResponseBuilder()
                .setCode(401)
                .setHeader("WWW-Authenticate", "Bearer")
                .build();
    }
}
