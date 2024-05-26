package org.burrow_studios.obelisk.util.crypto;

import org.junit.jupiter.api.Test;

import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class RSAEncryptionTest {
    private static final int KEY_LENGTH = 2048;
    private final RSAPrivateEncryption crypto = RSAPrivateEncryption.generate(KEY_LENGTH);

    @Test
    void testBasicPhrase() throws EncryptionException {
        String message = "Hello World";

        byte[] encrypted = crypto.encrypt(message.getBytes());
        byte[] decrypted = crypto.decrypt(encrypted);

        assertEquals(message, new String(decrypted));
    }

    @Test
    void testUnequal() throws EncryptionException {
        String message = "Hello World";

        byte[] encrypted = crypto.encrypt(message.getBytes());

        assertNotEquals(message, new String(encrypted));
    }

    @Test
    void testSingleCharacter() throws EncryptionException {
        String message = "a";

        byte[] encrypted = crypto.encrypt(message.getBytes());
        byte[] decrypted = crypto.decrypt(encrypted);

        assertEquals(message, new String(decrypted));
    }

    @Test
    void testEmptyString() throws EncryptionException {
        String message = "";

        byte[] encrypted = crypto.encrypt(message.getBytes());
        byte[] decrypted = crypto.decrypt(encrypted);

        assertEquals(message, new String(decrypted));
    }

    @Test
    void testLongString() throws EncryptionException {
        String message = "a".repeat(KEY_LENGTH / 8 - 11);

        byte[] encrypted = crypto.encrypt(message.getBytes());
        byte[] decrypted = crypto.decrypt(encrypted);

        assertEquals(message, new String(decrypted));
    }

    @Test
    void testFailOnLongerString() {
        String message = "a".repeat(KEY_LENGTH / 8 - 11 + 1);

        assertThrows(EncryptionException.class, () -> crypto.encrypt(message.getBytes()));
    }

    @Test
    void testFailOnNullArgs() {
        assertThrows(EncryptionException.class, () -> crypto.encrypt(null));
        assertThrows(EncryptionException.class, () -> crypto.decrypt(null));
    }

    @Test
    void testRecreateFromPubKey() throws EncryptionException {
        String message = "Hello World";
        RSAPublicKey pubKey = crypto.getPublicKey();
        RSAPublicEncryption crypto1 = new RSAPublicEncryption(pubKey);

        byte[] encrypted = crypto1.encrypt(message.getBytes());
        byte[] decrypted = crypto.decrypt(encrypted);

        assertEquals(message, new String(decrypted));
    }

    @Test
    void testFailDecryptionOnPublicInstance() throws EncryptionException {
        String message = "Hello World";
        RSAPublicKey pubKey = crypto.getPublicKey();
        RSAPublicEncryption crypto1 = new RSAPublicEncryption(pubKey);

        byte[] encrypted = crypto1.encrypt(message.getBytes());

        assertThrows(EncryptionException.class, () -> crypto1.decrypt(encrypted), "Decryption not supported");
    }
}
