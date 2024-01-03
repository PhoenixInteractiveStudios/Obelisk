package org.burrow_studios.obelisk.server.net.http.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class ForbiddenException extends APIException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(JWTVerificationException cause) {
        super(cause);
    }
}
