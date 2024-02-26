package org.burrow_studios.obelisk.moderationservice.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException() {

    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
