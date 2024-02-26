package org.burrow_studios.obelisk.commons.rpc.authentication;

/**
 * Level of authentication that is required to access a specific endpoint.
 * @see org.burrow_studios.obelisk.commons.rpc.Endpoint
 * @see org.burrow_studios.obelisk.commons.rpc.RPCRequest
 */
public enum AuthenticationLevel {
    /** No authentication is required. The endpoint is public. */
    NONE,
    /** Identity authentication is required. The requester must provide a valid identity token. */
    IDENTITY,
    /** Session authentication is required. The requester must provide a valid session token. */
    SESSION
}
