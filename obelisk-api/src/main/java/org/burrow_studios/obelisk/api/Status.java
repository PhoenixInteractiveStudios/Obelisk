package org.burrow_studios.obelisk.api;

public enum Status {
    /** Local initialization. Network connections have yet to be established. */
    PRE_INIT,
    /** Handshaking with server, establishing socket connection and building caches. */
    INIT,
    /** Ready to process requests and handle events. */
    READY,
    /** Received stop command; Awaiting pending requests & terminating connections. */
    STOPPING,
    /** All connection closed; Request cannot be processed anymore. */
    STOPPED
}
