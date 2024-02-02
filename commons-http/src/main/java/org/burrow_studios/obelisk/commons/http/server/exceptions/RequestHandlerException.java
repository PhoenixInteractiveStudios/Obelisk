package org.burrow_studios.obelisk.commons.http.server.exceptions;

import org.burrow_studios.obelisk.commons.http.server.Response;

public abstract class RequestHandlerException extends Exception {
    public RequestHandlerException() { }

    public RequestHandlerException(String message) {
        super(message);
    }

    public RequestHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandlerException(Throwable cause) {
        super(cause);
    }

    public abstract Response asResponse();
}
