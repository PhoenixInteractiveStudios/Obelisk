package org.burrow_studios.obelisk.gateway.net;

import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.rpc.http.SunServerImpl;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.ObeliskGateway;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkHandler implements Closeable {
    private static final Logger LOG = Logger.getLogger(NetworkHandler.class.getSimpleName());

    private final ObeliskGateway gateway;
    private final SunServerImpl server;

    public NetworkHandler(@NotNull ObeliskGateway gateway, @NotNull YamlSection config) throws IOException {
        this.gateway = gateway;

        LOG.log(Level.INFO, "Starting server");
        this.server = new SunServerImpl(
                config.getAsPrimitive("port").getAsInt(),
                gateway.getAuthenticationService(),
                gateway.getAuthorizationService()
        );

        LOG.log(Level.INFO, "Listening on port " + server.getPort());
    }

    public void registerEndpoint(@NotNull Endpoint endpoint, @NotNull EndpointHandler handler) throws IllegalArgumentException {
        this.validateRoute(endpoint);
        LOG.log(Level.INFO, "Registering endpoint: " + endpoint);
        this.server.addEndpoint(endpoint, handler);
    }

    public void unregisterEndpoint(@NotNull Endpoint endpoint) throws IllegalArgumentException {
        this.validateRoute(endpoint);
        LOG.log(Level.INFO, "Unregistering endpoint: " + endpoint);
        this.server.removeEndpoint(endpoint);
    }

    private void validateRoute(@NotNull Endpoint endpoint) throws IllegalArgumentException {
        // for now, the gateway does not handle any endpoints that are not routed; therefore any endpoint is a valid route
    }

    @Override
    public void close() throws IOException {
        LOG.log(Level.WARNING, "Shutting down");
        this.server.close();
        LOG.log(Level.INFO, "OK bye");
    }
}
