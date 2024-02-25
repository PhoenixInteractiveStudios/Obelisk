package org.burrow_studios.obelisk.gateway.net;

import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.EndpointHandler;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.rpc.http.SunServerImpl;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.ObeliskGateway;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

public class NetworkHandler implements Closeable {
    private final ObeliskGateway gateway;
    private final RPCServer<?> server;

    public NetworkHandler(@NotNull ObeliskGateway gateway, @NotNull YamlSection config) throws IOException {
        this.gateway = gateway;
        this.server = new SunServerImpl(config.getAsPrimitive("port").getAsInt(), /* TODO */ Authenticator.DENY_ALL);
    }

    public void registerEndpoint(@NotNull Endpoint endpoint, @NotNull EndpointHandler handler) throws IllegalArgumentException {
        this.validateRoute(endpoint);
        this.server.addEndpoint(endpoint, handler);
    }

    public void unregisterEndpoint(@NotNull Endpoint endpoint) throws IllegalArgumentException {
        this.validateRoute(endpoint);
        this.server.removeEndpoint(endpoint);
    }

    private void validateRoute(@NotNull Endpoint endpoint) throws IllegalArgumentException {
        // for now, the gateway does not handle any endpoints that are not routed; therefore any endpoint is a valid route
    }

    @Override
    public void close() throws IOException {
        this.server.close();
    }
}
