package org.burrow_studios.obelisk.util.crypto;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RSAPublicEncryption implements Encryption {
    private final RSAPublicKey publicKey;

    public RSAPublicEncryption(byte[] publicKey) throws EncryptionException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new EncryptionException(e);
        }
    }

    public RSAPublicEncryption(@NotNull RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public byte[] encrypt(byte[] data) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new EncryptionException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] data) throws EncryptionException {
        throw new EncryptionException("Decryption not supported");
    }

    public @NotNull RSAPublicKey getPublicKey() {
        return this.publicKey;
    }
}
