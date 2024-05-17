package org.burrow_studios.obelisk.monolith.http.exceptions;

import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

public abstract class RequestHandlerException extends Exception {
    public RequestHandlerException() {
    }

    public RequestHandlerException(String message) {
        super(message);
    }

    public RequestHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandlerException(Throwable cause) {
        super(cause);
    }

    public abstract @NotNull Response toResponse();
}
