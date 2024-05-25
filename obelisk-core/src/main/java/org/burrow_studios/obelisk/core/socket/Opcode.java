package org.burrow_studios.obelisk.core.socket;

/** Packet type identifiers. */
public enum Opcode {
    /* META */
    /** Ends a session and closes the connection. */
    DISCONNECT(0, false),
    /** Initial handshake, sent by the server */
    HELLO(1, false),
    /** Initial handshake, sent by the client. Begins encryption */
    IDENTIFY(2, false),

    /* HEARTBEAT */
    /** Periodically sent by the client to keep a connection alive. */
    HEARTBEAT(10, false),
    /** Acknowledges a heartbeat; Sent by the server. */
    HEARTBEAT_ACK(11, false),

    /* EVENTS */
    /** An entity create event. */
    CREATE_EVENT(20, true),
    /** An entity delete event. */
    DELETE_EVENT(21, true),
    /** An entity update event. */
    UPDATE_EVENT(22, true),

    /* CACHE REQUESTS */
    /** Requests all cacheable data from the server. */
    CACHE_REQUEST(30, true),
    /** Part of the cacheable data, sent as a response to a CACHE_REQUEST. */
    ENTITY_DATA(31, true),
    /** Informs the client that all ENTITY_DATA packets have been sent. */
    CACHE_DONE(32, true);

    private final int code;
    private final boolean encrypted;

    Opcode(int code, boolean encrypted) {
        this.code = code;
        this.encrypted = encrypted;
    }

    public int getCode() {
        return this.code;
    }

    public boolean isEncrypted() {
        return this.encrypted;
    }

    public static Opcode get(int code) {
        for (Opcode value : Opcode.values())
            if (value.code == code)
                return value;
        throw new IllegalArgumentException("Unknown opcode: " + code);
    }
}
