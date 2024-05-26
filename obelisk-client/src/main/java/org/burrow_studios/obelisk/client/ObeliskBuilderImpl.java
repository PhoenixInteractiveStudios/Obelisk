package org.burrow_studios.obelisk.client;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.ObeliskBuilder;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.GatewayConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.jetbrains.annotations.NotNull;

public class ObeliskBuilderImpl implements ObeliskBuilder {
    private String host;
    private String token;
    private String gatewayHost;
    private Integer gatewayPort;

    private ObeliskBuilderImpl() { }

    // DO NOT REMOVE
    // this method is called by the ObeliskBuilder interface via reflections
    public static @NotNull ObeliskBuilderImpl create() {
        return new ObeliskBuilderImpl();
    }

    @Override
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

    @Override
    public @NotNull ObeliskBuilderImpl setHost(String host) {
        this.host = host;
        return this;
    }

    @Override
    public @NotNull ObeliskBuilderImpl setGateway(String host, int port) {
        this.gatewayHost = host;
        this.gatewayPort = port;
        return this;
    }

    @Override
    public @NotNull ObeliskBuilderImpl setToken(String token) {
        this.token = token;
        return this;
    }
}
