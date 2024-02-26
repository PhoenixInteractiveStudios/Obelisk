package org.burrow_studios.obelisk.commons.rpc;

/**
 * Methods may be described as verbs, as they indicate <i>what</i> a {@link RPCRequest request} wants to do with a
 * resource.
 * <p> This enum broadly aligns with HTTP methods. This should however not be relied upon.
 */
public enum Method {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH
}
