package org.burrow_studios.obelisk.commons.http.server.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.burrow_studios.obelisk.commons.http.server.Response;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;

public class ForbiddenException extends RequestHandlerException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(JWTVerificationException cause) {
        super(cause);
    }

    @Override
    public Response asResponse() {
        return new ResponseBuilder()
                .setCode(403)
                .build();
    }
}
