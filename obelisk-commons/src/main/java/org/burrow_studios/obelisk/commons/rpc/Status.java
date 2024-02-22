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
}
