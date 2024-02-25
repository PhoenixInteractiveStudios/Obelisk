package org.burrow_studios.obelisk.shelly.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.burrow_studios.obelisk.commons.crypto.PassGen;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.burrow_studios.obelisk.shelly.Shelly;
import org.burrow_studios.obelisk.shelly.database.AuthDB;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenManager {
    private static final Logger LOG = Logger.getLogger(TokenManager.class.getSimpleName());

    private final TurtleGenerator turtleGenerator;
    private final Shelly shelly;
    private final KeyManager keyManager;

    public TokenManager(@NotNull Shelly shelly) throws IOException {
        this.turtleGenerator = TurtleGenerator.get("Shelly");
        this.shelly = shelly;
        this.keyManager = new KeyManager(this);
    }

    public @NotNull PublicKey getPublicIdentityKey(long id) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return this.keyManager.getPublicIdentityKey(id);
    }

    public @NotNull PublicKey getCurrentPublicSessionKey() {
        return this.keyManager.getCurrentPublicSessionKey();
    }

    public @NotNull String newIdentityToken(long subject, boolean newFamily) {
        final long id = turtleGenerator.newId();
        final Algorithm algorithm;

        try {
            algorithm = keyManager.newIdentityAlgorithm(id);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not generate new token for subject " + id + " due to an internal encryption exception.", e);
            throw new RuntimeException("Internal encryption exception");
        }

        final String token = JWT.create()
                .withIssuer("Shelly")
                .withSubject(Long.toString(subject))
                .withJWTId(Long.toString(id))
                .sign(algorithm);

        AuthDB database = shelly.getDatabase();
        if (newFamily)
            database.invalidateIdentityTokenFamily(subject);
        database.createIdentity(id, subject);

        return token;
    }

    public @NotNull String newSessionToken(long identity, long subject) {
        final long id = turtleGenerator.newId();
        final Algorithm algorithm = keyManager.getCurrentSessionAlgorithm();

        final String sok = PassGen.generate(32);

        final String token = JWT.create()
                .withKeyId(Long.toString(identity))
                .withIssuer("Shelly")
                .withSubject(Long.toString(subject))
                .withJWTId(Long.toString(id))
                .withClaim("sok", sok)
                .sign(algorithm);

        AuthDB database = shelly.getDatabase();
        database.invalidateAllSessions(identity);
        database.createSession(id, identity, token);

        return token;
    }

    public @NotNull DecodedJWT decodeIdentityToken(String token) throws JWTVerificationException {
        DecodedJWT decode = JWT.decode(token);

        String idStr = decode.getId();
        long   id;

        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new InvalidClaimException("Invalid token id");
        }

        long time = TurtleGenerator.getTime(id);
        if (time > System.currentTimeMillis())
            throw new InvalidClaimException("Invalid token id. Timestamp is from the future.");

        Algorithm algorithm;
        try {
            algorithm = keyManager.getPublicIdentityVerificationAlgorithm(id);
        } catch (Exception e) {
            throw new JWTVerificationException("Unable to resolve algorithm");
        }

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Shelly")
                .withClaimPresence("sub")
                .withClaimPresence("exp")
                .ignoreIssuedAt()
                .build();

        AuthDB database = shelly.getDatabase();
        // TODO: validate db entries

        return verifier.verify(decode);
    }

    public @NotNull DecodedJWT decodeSessionToken(String token) throws JWTVerificationException {
        DecodedJWT decode = JWT.decode(token);

        String idStr = decode.getId();
        long   id;

        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            throw new InvalidClaimException("Invalid token id");
        }

        long time = TurtleGenerator.getTime(id);
        if (time > System.currentTimeMillis())
            throw new InvalidClaimException("Invalid token id. Timestamp is from the future.");

        Algorithm algorithm = keyManager.getCurrentSessionAlgorithm();

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Shelly")
                .withClaimPresence("sub")
                .withClaimPresence("exp")
                .withClaimPresence("aud")
                .withClaimPresence("sok")
                .build();

        AuthDB database = shelly.getDatabase();
        // TODO: validate db entries
        // TODO: validate key id

        return verifier.verify(decode);
    }

    public @NotNull String getSocketKey(long sub, long sid) {
        String token = this.shelly.getDatabase().getSocketKey(sid);

        return JWT.decode(token)
                .getClaim("sok")
                .asString();
    }
}
