package org.burrow_studios.obelisk.monolith.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.burrow_studios.obelisk.monolith.exceptions.AuthenticationException;
import org.jetbrains.annotations.NotNull;

import java.security.interfaces.RSAPublicKey;

public final class ApplicationContext {
    private final AuthManager manager;
    private final ApplicationData data;
    private final DecodedJWT token;

    public ApplicationContext(@NotNull AuthManager manager, @NotNull ApplicationData data, @NotNull DecodedJWT token) throws AuthenticationException {
        this.manager = manager;
        this.data = data;
        this.token = token;

        this.manager.verify(this);
    }

    public @NotNull AuthManager getManager() {
        return this.manager;
    }

    public long getId() {
        return this.data.getId();
    }

    public @NotNull String getName() {
        return this.data.getName();
    }

    public @NotNull RSAPublicKey getPubKey() {
        return this.data.getPubKey();
    }

    public @NotNull DecodedJWT getToken() {
        return this.token;
    }

    public boolean hasIntent(@NotNull String intent) {
        for (Intent i : this.data.getIntents())
            if (i.name().equals(intent))
                return true;
        return false;
    }
}
