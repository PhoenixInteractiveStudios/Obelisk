package org.burrow_studios.obelisk.commons.rpc;

public enum Status {
    // SUCCESS
    OK,
    CREATED,
    NO_CONTENT,

    // REDIRECTION
    MOVED_PERMANENTLY,
    FOUND,

    // CLIENT ERROR
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    METHOD_NOT_ALLOWED,

    // SERVER ERROR
    INTERNAL_SERVER_ERROR,
    NOT_IMPLEMENTED,
    BAD_GATEWAY,
    GATEWAY_TIMEOUT

    ;
}
