package org.burrow_studios.obelisk.util.crypto;

public class EncryptionException extends Exception {
    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(Throwable cause) {
        super(cause);
    }
}
