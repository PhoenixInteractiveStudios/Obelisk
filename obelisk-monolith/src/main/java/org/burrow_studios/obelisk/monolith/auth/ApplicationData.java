package org.burrow_studios.obelisk.monolith.auth;

import org.jetbrains.annotations.NotNull;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.Set;

import static com.auth0.jwt.HeaderParams.ALGORITHM;

public class ApplicationData {
    private final long id;
    private final String name;
    private final RSAPublicKey pubKey;
    private final String pubKeyAsString;
    private final Set<Intent> intents;

    public ApplicationData(long id, @NotNull String name, @NotNull String pubKey, @NotNull Set<Intent> intents) throws IllegalArgumentException {
        this.id = id;
        this.name = name;
        this.intents = Set.copyOf(intents);

        try {
            byte[] keyBytes = Base64.getDecoder().decode(pubKey);
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            this.pubKey = (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalArgumentException("Illegal pubKey", e);
        }

        byte[] encoded = Base64.getEncoder().encode(this.pubKey.getEncoded());
        this.pubKeyAsString = new String(encoded);
    }

    public long getId() {
        return this.id;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull RSAPublicKey getPubKey() {
        return this.pubKey;
    }

    public @NotNull String getPubKeyAsString() {
        return this.pubKeyAsString;
    }

    public @NotNull Set<Intent> getIntents() {
        return Collections.unmodifiableSet(this.intents);
    }
}
