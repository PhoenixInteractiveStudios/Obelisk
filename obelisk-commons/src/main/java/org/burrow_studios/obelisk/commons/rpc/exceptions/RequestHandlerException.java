package org.burrow_studios.obelisk.commons.rpc.exceptions;

import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.jetbrains.annotations.NotNull;

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

    public abstract RPCResponse asResponse(@NotNull RPCRequest request);
}
