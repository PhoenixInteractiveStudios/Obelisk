package org.burrow_studios.obelisk.monolith.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.burrow_studios.obelisk.monolith.Main;
import org.burrow_studios.obelisk.monolith.db.impl.AuthDatabase;
import org.burrow_studios.obelisk.monolith.db.interfaces.AuthDB;
import org.burrow_studios.obelisk.monolith.exceptions.AuthenticationException;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AuthManager {
    private final AuthDB database;
    private final ConcurrentHashMap<Long, ApplicationContext> appCache;

    public AuthManager() throws DatabaseException {
        this.database = new AuthDatabase(new File(Main.DIR, "auth.db"));
        this.appCache = new ConcurrentHashMap<>();
    }

    public @NotNull ApplicationContext authenticate(String token) throws AuthenticationException {
        if (token == null)
            throw new AuthenticationException();

        DecodedJWT decode;
        ApplicationData appData;

        try {
            decode = JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new AuthenticationException();
        }

        Long appId = decode.getClaim("app").asLong();
        if (appId == null)
            throw new AuthenticationException();

        ApplicationContext cachedApp = this.appCache.get(appId);
        if (cachedApp != null) {
            if (!Objects.equals(cachedApp.getToken().getToken(), token))
                throw new AuthenticationException();
            return cachedApp;
        }

        try {
            appData = this.database.getApplication(appId);
        } catch (DatabaseException e) {
            throw new AuthenticationException();
        }

        return new ApplicationContext(this, appData, decode);
    }

    public void verify(@NotNull ApplicationContext appCtx) throws AuthenticationException {
        Algorithm algorithm = Algorithm.RSA256(appCtx.getPubKey());

        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("app", appCtx.getId())
                .build();

        try {
            verifier.verify(appCtx.getToken());
        } catch (JWTVerificationException e) {
            throw new AuthenticationException();
        }

        this.appCache.put(appCtx.getId(), appCtx);
    }

    public @NotNull AuthDB getDatabase() {
        return this.database;
    }
}
