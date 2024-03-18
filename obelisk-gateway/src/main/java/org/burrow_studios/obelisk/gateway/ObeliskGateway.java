package org.burrow_studios.obelisk.gateway;

import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.util.ResourceTools;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.commons.yaml.YamlUtil;
import org.burrow_studios.obelisk.gateway.authentication.AuthenticationService;
import org.burrow_studios.obelisk.gateway.authorization.AuthorizationService;
import org.burrow_studios.obelisk.gateway.net.NetworkHandler;
import org.burrow_studios.obelisk.gateway.service.Service;
import org.burrow_studios.obelisk.gateway.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObeliskGateway {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final @NotNull YamlSection config;
    private final @NotNull File configFile = new File(Main.DIR, "config.yaml");

    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;
    private final NetworkHandler networkHandler;
    private final ServiceRegistry serviceRegistry;

    ObeliskGateway() throws IOException, TimeoutException {
        LOG.log(Level.INFO, "Loading config");

        ResourceTools resourceTools = ResourceTools.get(Main.class);
        YamlUtil.saveDefault(configFile, resourceTools.getResource("config.yaml"));

        this.config = YamlUtil.load(configFile, YamlSection.class);

        LOG.log(Level.INFO, "Enabling authentication");
        this.authenticationService = new AuthenticationService(this, config.getAsSection("authentication"));
        LOG.log(Level.INFO, "Enabling authorization");
        this.authorizationService  = new  AuthorizationService(this, config.getAsSection("authorization"));

        LOG.log(Level.INFO, "Initiating NetworkHandler");
        this.networkHandler  = new  NetworkHandler(this, config.getAsSection("net"));
        LOG.log(Level.INFO, "Initiating ServiceRegistry");
        this.serviceRegistry = new ServiceRegistry(this, config.getAsSection("registry"));

        LOG.log(Level.INFO, "Startup complete!");
    }

    void stop() throws IOException {
        LOG.log(Level.WARNING, "Shutting down");
        this.config.save(configFile);
        this.networkHandler.close();
        this.serviceRegistry.close();
        LOG.log(Level.INFO, "OK bye");
    }

    public @NotNull AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public @NotNull AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public @NotNull RPCResponse handleInternal(@NotNull Endpoint endpoint, @NotNull RPCRequest request) throws RequestHandlerException {
        Service service = this.serviceRegistry.getServiceByEndpoint(endpoint);
        if (service == null)
            throw new NotFoundException();
        return service.handleInternal(request);
    }
}
