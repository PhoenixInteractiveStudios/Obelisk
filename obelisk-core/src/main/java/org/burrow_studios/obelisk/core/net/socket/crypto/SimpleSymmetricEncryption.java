package org.burrow_studios.obelisk.core.net.socket.crypto;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public final class SimpleSymmetricEncryption implements EncryptionHandler {
    /** Encryption algorithm */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final int KEY_SPEC_PASS_ITERATIONS = 65536;
    private static final int KEY_SPEC_KEY_LENGTH      = 256;

    private static final int   IV_LENGTH = 16;
    private static final int SALT_LENGTH = 16;

    private final char[] key;

    public SimpleSymmetricEncryption(char[] key) {
        this.key = key;
    }

    /** Takes in a pass and a salt value and generates a {@link SecretKey}. */
    private @NotNull SecretKey generateKey(byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(this.key, salt, KEY_SPEC_PASS_ITERATIONS, KEY_SPEC_KEY_LENGTH);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    /** Generates an IV from {@value IV_LENGTH} random bytes. */
    private static @NotNull IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /** Generates {@value SALT_LENGTH} random bytes as salt. */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    @Override
    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            return this.encrypt0(data);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data) throws EncryptionException {
        try {
            return this.decrypt0(data);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private byte[] encrypt0(byte[] data) throws Exception {
        byte[] sa = generateSalt();

        final IvParameterSpec ivParameterSpec = generateIv();
        final SecretKey key = generateKey(sa);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        final byte[] iv  = ivParameterSpec.getIV();
        final byte[] enc = cipher.doFinal(data);
        final byte[] out = new byte[iv.length + sa.length + enc.length];

        System.arraycopy(iv , 0, out, 0, iv.length);
        System.arraycopy(sa , 0, out, iv.length, sa.length);
        System.arraycopy(enc, 0, out, iv.length + sa.length, enc.length);

        return Base64.getEncoder().encode(out);
    }

    private byte[] decrypt0(byte[] data) throws Exception {
        final byte[] bytes = Base64.getDecoder().decode(data);

        final byte[] iv  = Arrays.copyOfRange(bytes, 0, IV_LENGTH);
        final byte[] sa  = Arrays.copyOfRange(bytes, IV_LENGTH, IV_LENGTH + SALT_LENGTH);
        final byte[] enc = Arrays.copyOfRange(bytes, IV_LENGTH + SALT_LENGTH, bytes.length);

        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        final SecretKey key = generateKey(sa);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

        return cipher.doFinal(enc);
    }
}
