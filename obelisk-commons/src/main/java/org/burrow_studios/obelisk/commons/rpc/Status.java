package org.burrow_studios.obelisk.commons.rpc;

public enum Status {
    // SUCCESS
    OK(200),
    CREATED(201),
    NO_CONTENT(204),

    // REDIRECTION
    MOVED_PERMANENTLY(301),
    FOUND(302),

    // CLIENT ERROR
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),

    // SERVER ERROR
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    GATEWAY_TIMEOUT(504),

    ;

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }

    public boolean isRedirect() {
        return code >= 300 && code < 400;
    }

    public boolean isClientError() {
        return code >= 400 && code < 500;
    }

    public boolean isServerError() {
        return code >= 500 && code < 600;
    }

    public boolean isError() {
        return isClientError() || isServerError();
    }
}
