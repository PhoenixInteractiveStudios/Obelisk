package org.burrow_studios.obelisk.commons.http;

/** Authentication level that is required for an {@link Endpoint}. */
public enum AuthLevel {
    /** The Endpoint requires a valid identity token. */
    IDENTITY,
    /** The Endpoint required a valid session token. */
    SESSION,
    /** The Endpoint does not require any authentication. */
    NONE
}
