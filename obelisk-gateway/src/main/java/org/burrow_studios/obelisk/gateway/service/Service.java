package org.burrow_studios.obelisk.gateway.service;

import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadGatewayException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.GatewayTimeoutException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.gateway.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Service implements EndpointHandler, Closeable {
    private final ServiceRegistry registry;
    private final RPCClient proxyClient;
    private final String name;
    private final Set<Endpoint> endpoints;

    public Service(@NotNull ServiceRegistry registry, @NotNull RPCClient client, @NotNull String name, @NotNull Set<Endpoint> endpoints) {
        this.registry = registry;
        this.proxyClient = client;
        this.name = name;
        this.endpoints = new HashSet<>(endpoints);

        NetworkHandler networkHandler = registry.getGateway().getNetworkHandler();
        for (Endpoint endpoint : endpoints)
            networkHandler.registerEndpoint(endpoint, this);
    }

    @Override
    public void handle(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        TimeoutContext timeout = request.getTimeout();
        if (timeout.asTimeout() > TimeoutContext.DEFAULT.asTimeout() * 2)
            timeout = TimeoutContext.timeout(TimeoutContext.DEFAULT.asTimeout() * 2);

        RPCRequest proxyRequest = new RPCRequest.Builder()
                .setMethod(request.getMethod())
                .setPath(request.getPath())
                .setBody(request.getBody())
                .setTimeout(timeout)
                .build(request.getId());

        RPCResponse proxyResponse = this.handleInternal(proxyRequest);

        response.setStatus(proxyResponse.getStatus());
        response.setBody(proxyResponse.getBody());
    }

    public @NotNull RPCResponse handleInternal(@NotNull RPCRequest request) throws RequestHandlerException {
        try {
            return this.proxyClient.send(request).get(request.getTimeout().asTimeout(), TimeUnit.MILLISECONDS);
        } catch (CancellationException | ExecutionException | InterruptedException e) {
            throw new BadGatewayException();
        } catch (TimeoutException e) {
            throw new GatewayTimeoutException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    public @NotNull String getName() {
        return name;
    }

    public Set<Endpoint> getEndpoints() {
        return endpoints;
    }

    @Override
    public void close() throws IOException {
        NetworkHandler networkHandler = registry.getGateway().getNetworkHandler();
        for (Endpoint endpoint : this.endpoints)
            networkHandler.unregisterEndpoint(endpoint);
        this.proxyClient.close();
    }
}
