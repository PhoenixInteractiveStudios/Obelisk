package org.burrow_studios.obelisk.client;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.GatewayConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.jetbrains.annotations.NotNull;

public class ObeliskBuilder {
    private String host;
    private String token;
    private String gatewayHost;
    private Integer gatewayPort;

    private ObeliskBuilder() { }

    public static @NotNull ObeliskBuilder create() {
        return new ObeliskBuilder();
    }

    public static @NotNull ObeliskBuilder createDefault() {
        return new ObeliskBuilder()
                .setHost("api.burrow-studios.org/v1/");
    }

    public @NotNull Obelisk build() throws IllegalArgumentException {
        if (host == null)
            throw new IllegalArgumentException("Host must be specified");
        if (token == null)
            throw new IllegalArgumentException("Token must be specified");

        AuthConfig authConfig = new AuthConfig(token);
        HttpConfig httpConfig = new HttpConfig(host);
        GatewayConfig gatewayConfig = new GatewayConfig();

        if (this.gatewayHost != null)
            gatewayConfig.setHost(gatewayHost);
        if (this.gatewayPort != null)
            gatewayConfig.setPort(gatewayPort);

        ObeliskImpl obelisk = new ObeliskImpl(authConfig, httpConfig, gatewayConfig);

        obelisk.login();

        return obelisk;
    }

    public @NotNull ObeliskBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public @NotNull ObeliskBuilder setGateway(String host, int port) {
        this.gatewayHost = host;
        this.gatewayPort = port;
        return this;
    }

    public @NotNull ObeliskBuilder setToken(String token) {
        this.token = token;
        return this;
    }
}
