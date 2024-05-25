package org.burrow_studios.obelisk.util.crypto;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SimpleSymmetricEncryptionTest {
    private static final String PASS = "a1.-X*#++";

    private final SimpleSymmetricEncryption crypto = new SimpleSymmetricEncryption(PASS.toCharArray());

    @Test
    void testBasicPhrase() throws EncryptionException {
        doTest("Hello World!");
    }

    @Test
    void testSingleCharacter() throws EncryptionException {
        doTest("a");
    }

    @Test
    void testEmptyString() throws EncryptionException {
        doTest("");
    }

    @Test
    void testLongString() throws EncryptionException {
        doTest("a".repeat(Short.MAX_VALUE));
    }

    @Test
    void testFailOnNullArgs() {
        assertThrows(EncryptionException.class, () -> crypto.encrypt(null));
        assertThrows(EncryptionException.class, () -> crypto.decrypt(null));
    }

    @Test
    void monkeyTest() throws EncryptionException {
        final int attempts  = 100;
        final int maxLength = 10000;

        byte[] message, encrypted, decrypted;

        Random random = new Random();

        for (int i = 0; i < attempts; i++) {
            message = new byte[random.nextInt(maxLength)];
            random.nextBytes(message);

            encrypted = crypto.encrypt(message);
            decrypted = crypto.decrypt(encrypted);

            assertArrayEquals(message, decrypted);
        }
    }

    private void doTest(String message) throws EncryptionException {
        byte[] encrypted = crypto.encrypt(message.getBytes());
        byte[] decrypted = crypto.decrypt(encrypted);

        String result = new String(decrypted);

        assertEquals(message, result);
    }
}