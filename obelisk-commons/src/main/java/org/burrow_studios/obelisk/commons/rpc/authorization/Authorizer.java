package org.burrow_studios.obelisk.commons.rpc.authorization;

import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Authorizer {
    Authorizer ALLOW_ALL = (token, intents) -> { };
    Authorizer  DENY_ALL = (token, intents) -> { throw new ForbiddenException(); };

    Authorizer PUBLIC_ONLY = (token, intents) -> {
        if (intents.length != 0)
            throw new ForbiddenException();
    };

    void authorize(@NotNull String token, @NotNull String[] intents) throws InternalServerErrorException, ForbiddenException;
}
