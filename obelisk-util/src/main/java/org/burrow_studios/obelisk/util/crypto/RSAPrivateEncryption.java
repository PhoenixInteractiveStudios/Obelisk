package org.burrow_studios.obelisk.util.crypto;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAPrivateEncryption implements Encryption {
    private static final String ALGORITHM = "RSA";

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey  publicKey;

    public RSAPrivateEncryption(@NotNull KeyPair keyPair) {
        this((RSAPrivateKey) keyPair.getPrivate(), (RSAPublicKey) keyPair.getPublic());
    }

    public RSAPrivateEncryption(@NotNull RSAPrivateKey privateKey, @NotNull RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IllegalArgumentException e) {
            throw new EncryptionException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IllegalArgumentException e) {
            throw new EncryptionException(e);
        }
    }

    public static @NotNull RSAPrivateEncryption generate(int keyLength) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(keyLength);

            final KeyPair keyPair = generator.generateKeyPair();

            final PublicKey  publicKey  = keyPair.getPublic();
            final PrivateKey privateKey = keyPair.getPrivate();

            return new RSAPrivateEncryption(
                    (RSAPrivateKey) privateKey,
                    (RSAPublicKey)  publicKey
            );
        } catch (NoSuchAlgorithmException e) {
            throw new Error();
        }
    }

    public @NotNull RSAPublicKey getPublicKey() {
        return this.publicKey;
    }
}
