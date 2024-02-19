package org.burrow_studios.obelisk.commons.http.server;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Authorizer {
    @NotNull DecodedJWT authorizeIdentity(@NotNull String token) throws JWTVerificationException;

    @NotNull DecodedJWT authorizeSession(@NotNull String token) throws JWTVerificationException;

    static @NotNull Authorizer of(
            @NotNull Function<String, DecodedJWT> authorizeIdentity,
            @NotNull Function<String, DecodedJWT> authorizeSession
    ) {
        return new Authorizer() {
            @Override
            public @NotNull DecodedJWT authorizeIdentity(@NotNull String token) throws JWTVerificationException {
                return authorizeIdentity.apply(token);
            }

            @Override
            public @NotNull DecodedJWT authorizeSession(@NotNull String token) throws JWTVerificationException {
                return authorizeSession.apply(token);
            }
        };
    }
}
