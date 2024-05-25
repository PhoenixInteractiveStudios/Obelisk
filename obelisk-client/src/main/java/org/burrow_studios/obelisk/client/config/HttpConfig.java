package org.burrow_studios.obelisk.client.config;

import org.jetbrains.annotations.NotNull;

public class HttpConfig {
    private @NotNull String baseUrl;

    public HttpConfig(@NotNull String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public @NotNull String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(@NotNull String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
