package org.burrow_studios.obelisk.client.config;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

public class HttpConfig {
    private @NotNull String baseUrl;
    private URI gatewayUrl;

    public HttpConfig(@NotNull String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public @NotNull String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(@NotNull String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URI getGatewayUrl() {
        return this.gatewayUrl;
    }

    public void setGatewayUrl(URI gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public boolean hasGatewayUrl() {
        return this.gatewayUrl != null;
    }
}
