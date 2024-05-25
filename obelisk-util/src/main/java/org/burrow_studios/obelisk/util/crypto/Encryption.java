package org.burrow_studios.obelisk.util.crypto;

public interface Encryption {
    Encryption NONE = new Encryption() {
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
