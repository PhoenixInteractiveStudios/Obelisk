package org.burrow_studios.obelisk.core.socket;

/** Packet type identifiers. */
public enum Opcode {
    /* META */
    /** Ends a session and closes the connection. */
    DISCONNECT(0),
    /** Initial handshake, sent by the server */
    HELLO(1),
    /** Initial handshake, sent by the client. Begins encryption */
    IDENTIFY(2),

    /* HEARTBEAT */
    /** Periodically sent by the client to keep a connection alive. */
    HEARTBEAT(10),
    /** Acknowledges a heartbeat; Sent by the server. */
    HEARTBEAT_ACK(11),

    /* EVENTS */
    /** An entity create event. */
    CREATE_EVENT(20),
    /** An entity delete event. */
    DELETE_EVENT(21),
    /** An entity update event. */
    UPDATE_EVENT(22),

    /* CACHE REQUESTS */
    /** Requests all cacheable data from the server. */
    CACHE_REQUEST(30),
    /** Part of the cacheable data, sent as a response to a CACHE_REQUEST. */
    ENTITY_DATA(31),
    /** Informs the client that all ENTITY_DATA packets have been sent. */
    CACHE_DONE(32);

    private final int code;

    Opcode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static Opcode get(int code) {
        for (Opcode value : Opcode.values())
            if (value.code == code)
                return value;
        throw new IllegalArgumentException("Unknown opcode: " + code);
    }
}
