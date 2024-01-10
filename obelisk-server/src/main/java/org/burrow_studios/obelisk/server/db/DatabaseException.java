package org.burrow_studios.obelisk.server.db;

public class DatabaseException extends RuntimeException {
    public DatabaseException() {
        super();
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
