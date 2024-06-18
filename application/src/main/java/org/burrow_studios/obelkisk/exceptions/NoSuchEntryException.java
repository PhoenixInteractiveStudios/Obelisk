package org.burrow_studios.obelkisk.exceptions;

public class NoSuchEntryException extends DatabaseException {
    public NoSuchEntryException() {
        super();
    }

    public NoSuchEntryException(String message) {
        super(message);
    }
}
