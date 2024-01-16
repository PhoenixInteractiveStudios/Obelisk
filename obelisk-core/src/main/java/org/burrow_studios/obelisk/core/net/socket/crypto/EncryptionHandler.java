package org.burrow_studios.obelisk.core.net.socket.crypto;

public interface EncryptionHandler {
    EncryptionHandler NONE = new EncryptionHandler() {
        @Override
        public byte[] encrypt(byte[] data) {
            return data;
        }

        @Override
        public byte[] decrypt(byte[] data) {
            return data;
        }
    };

    byte[] encrypt(byte[] data) throws EncryptionException;

    byte[] decrypt(byte[] data) throws EncryptionException;
}
