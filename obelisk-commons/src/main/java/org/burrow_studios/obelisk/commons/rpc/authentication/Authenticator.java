package org.burrow_studios.obelisk.commons.rpc.authentication;

import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Authenticator {
    Authenticator ALLOW_ALL = (token, level) -> { };
    Authenticator  DENY_ALL = (token, level) -> { throw new ForbiddenException(); };

    Authenticator PUBLIC_ONLY = (token, level) -> {
        if (level != AuthenticationLevel.NONE)
            throw new ForbiddenException();
    };

    void authenticate(@NotNull String token, @NotNull AuthenticationLevel level) throws ForbiddenException;
}
