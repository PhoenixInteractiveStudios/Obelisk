package org.burrow_studios.obelisk.server.auth.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.burrow_studios.obelisk.common.TurtleGenerator;
import org.burrow_studios.obelisk.server.auth.Authenticator;
import org.burrow_studios.obelisk.server.auth.db.AuthDB;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenManager {
    private static final Logger LOG = Logger.getLogger(TokenManager.class.getSimpleName());

    private final TurtleGenerator turtleGenerator;
    private final Authenticator authenticator;
    private final KeyManager keyManager;

    public TokenManager(@NotNull Authenticator authenticator) throws IOException {
        this.turtleGenerator = TurtleGenerator.get("Shelly");
        this.authenticator = authenticator;
        this.keyManager = new KeyManager(this);
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

        AuthDB database = authenticator.getDatabase();
        if (newFamily)
            database.invalidateIdentityTokenFamily(subject);
        database.createIdentity(id, subject);

        return token;
    }

    public @NotNull String newSessionToken(long identity, long subject) {
        final long id = turtleGenerator.newId();
        final Algorithm algorithm = keyManager.getCurrentSessionAlgorithm();

        final String token = JWT.create()
                .withKeyId(Long.toString(identity))
                .withIssuer("Shelly")
                .withSubject(Long.toString(subject))
                .withJWTId(Long.toString(id))
                .sign(algorithm);

        AuthDB database = authenticator.getDatabase();
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

        AuthDB database = authenticator.getDatabase();
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
                .build();

        AuthDB database = authenticator.getDatabase();
        // TODO: validate db entries
        // TODO: validate key id

        return verifier.verify(decode);
    }
}
