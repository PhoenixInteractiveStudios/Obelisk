package org.burrow_studios.obelisk.client.config;

import org.jetbrains.annotations.NotNull;

public class AuthConfig {
    private String token;

    public AuthConfig(@NotNull String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
