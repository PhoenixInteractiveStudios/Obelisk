package org.burrow_studios.obelisk.gateway.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPClient;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.rpc.authorization.Authorizer;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.rpc.http.HTTPClient;
import org.burrow_studios.obelisk.commons.rpc.http.SunServerImpl;
import org.burrow_studios.obelisk.commons.util.validation.Validation;
import org.burrow_studios.obelisk.commons.yaml.YamlPrimitive;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.ObeliskGateway;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceRegistry implements Closeable {
    private static final Logger LOG = Logger.getLogger(ServiceRegistry.class.getSimpleName());

    private static final String LEGAL_NAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String LEGAL_NAME_DELIMITERS = "-._";

    private final ObeliskGateway gateway;

    private final YamlSection config;
    private final RPCServer<?> server;

    private final ConcurrentHashMap<String, Service> services;

    public ServiceRegistry(@NotNull ObeliskGateway gateway, @NotNull YamlSection config) throws IOException, TimeoutException {
        this.gateway = gateway;
        this.config = config;

        this.services = new ConcurrentHashMap<>();

        YamlSection serverConfig = config.getAsSection("server");
        YamlPrimitive type = serverConfig.getAsPrimitive("type");

        LOG.log(Level.INFO, "Attempting to start server of type '" + type.getAsString() + "'.");

        this.server = switch (type.getAsString().toLowerCase()) {
            case "amqp" -> new AMQPServer(
                    serverConfig.getAsPrimitive("host").getAsString(),
                    serverConfig.getAsPrimitive("port").getAsInt(),
                    serverConfig.getAsPrimitive("user").getAsString(),
                    serverConfig.getAsPrimitive("pass").getAsString(),
                    "meta", "service_discovery",
                    // only exposed internally
                    Authenticator.ALLOW_ALL, Authorizer.ALLOW_ALL
            );
            case "http" -> new SunServerImpl(
                    serverConfig.getAsPrimitive("port").getAsInt(),
                    // only exposed internally
                    Authenticator.ALLOW_ALL, Authorizer.ALLOW_ALL
            );
            default -> throw new IllegalArgumentException("Unsupported server type: " + type.getAsString());
        };

        LOG.log(Level.INFO, "Server online. Creating endpoints...");

        this.server.addEndpoint(Endpoint.build(Method.POST  , "/services"        , AuthenticationLevel.NONE), this::onPost);
        this.server.addEndpoint(Endpoint.build(Method.DELETE, "/services/:string", AuthenticationLevel.NONE), this::onDelete);
    }

    @Override
    public void close() throws IOException {
        LOG.log(Level.WARNING, "Shutting down");
        this.server.close();
        for (Service service : this.services.values())
            service.close();
        LOG.log(Level.INFO, "OK bye");
    }

    private synchronized void onPost(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String name = request.bodyHelper().requireElementAsString("name");

        // name validation
        try {
            Validation.of("Service name", name)
                    .checkNonNull()
                    .checkNotBlank()
                    .checkLength(2, 65)
                    .checkCharWhitelist(LEGAL_NAME_CHARS, LEGAL_NAME_DELIMITERS);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Illegal service name");
        }

        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("name", name);

        RPCClient proxyClient = null;

        JsonObject connectionInfo = request.bodyHelper().requireElement("connection", JsonElement::getAsJsonObject);

        if (server instanceof AMQPServer) {
            // AMQP mode
            responseBody.addProperty("connection_type", "AMQP");

            YamlSection amqpConfig = this.config.getAsSection("server");
            final String host = amqpConfig.getAsPrimitive("host").getAsString();
            final int    port = amqpConfig.getAsPrimitive("port").getAsInt();
            final String user = amqpConfig.getAsPrimitive("user").getAsString();
            final String pass = amqpConfig.getAsPrimitive("pass").getAsString();

            if (!(connectionInfo.get("exchange") instanceof JsonPrimitive exchangeInfo))
                throw new BadRequestException("Malformed body: Missing exchange info");
            final String exchange = exchangeInfo.getAsString();

            if (!(connectionInfo.get("queue") instanceof JsonPrimitive queueInfo))
                throw new BadRequestException("Malformed body: Missing queue info");
            final String queue = queueInfo.getAsString();

            try {
                proxyClient = new AMQPClient(host, port, user, pass, exchange, queue);
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        if (server instanceof SunServerImpl) {
            // HTTP mode
            responseBody.addProperty("connection_type", "HTTP");

            if (!(connectionInfo.get("host") instanceof JsonPrimitive hostInfo))
                throw new BadRequestException("Malformed body: Missing host info");
            final String host = hostInfo.getAsString();

            try {
                proxyClient = new HTTPClient(host, null, null);
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        if (proxyClient == null)
            throw new InternalServerErrorException();

        JsonArray routesInfo = request.bodyHelper().requireElement("routes", JsonElement::getAsJsonArray);

        Set<Endpoint> endpoints = new LinkedHashSet<>();

        routes:
        for (JsonElement element : routesInfo) {
            if (!(element instanceof JsonObject routeInfo))
                throw new BadRequestException("Malformed body: Malformed routing info");

            if (!(routeInfo.get("method") instanceof JsonPrimitive methodInfo))
                throw new BadRequestException("Malformed body: Malformed routing info");
            final String methodStr = methodInfo.getAsString();
            final Method method;
            try {
                method = Method.valueOf(methodStr);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Malformed body: Malformed routing info");
            }

            if (!(routeInfo.get("path") instanceof JsonPrimitive pathInfo))
                throw new BadRequestException("Malformed body: Malformed routing info");
            final String path = pathInfo.getAsString();

            if (!(routeInfo.get("auth") instanceof JsonObject authInfo))
                throw new BadRequestException("Malformed body: Missing auth info");

            if (!(authInfo.get("authentication_level") instanceof JsonPrimitive authLevelInfo))
                throw new BadRequestException("Malformed body: Malformed auth info");
            final String authLevelStr = authLevelInfo.getAsString();
            final AuthenticationLevel authLevel = AuthenticationLevel.valueOf(authLevelStr);

            String[] intents = {};
            JsonElement intentInfo = authInfo.get("intents");
            if (intentInfo != null) {
                if (!(intentInfo instanceof JsonArray intentArr))
                    throw new BadRequestException("Malformed body: Malformed intent info");

                ArrayList<String> intentList = new ArrayList<>();
                for (JsonElement intent : intentArr)
                    intentList.add(intent.getAsString());
                intents = intentList.toArray(String[]::new);
            }

            Endpoint endpoint = Endpoint.build(method, path, authLevel, intents);

            for (Service service : this.services.values()) {
                for (Endpoint serviceEndpoint : service.getEndpoints()) {
                    if (!serviceEndpoint.equals(endpoint)) continue;
                    // CONFLICT! The existing route will be respected
                    continue routes;
                }
            }

            endpoints.add(endpoint);
        }

        final Service service;
        try {
            service = new Service(this, proxyClient, name, endpoints);
        } catch (Exception e) {
            // undo registration before failing
            for (Endpoint endpoint : endpoints)
                this.gateway.getNetworkHandler().unregisterEndpoint(endpoint);
            throw new InternalServerErrorException();
        }

        this.services.put(name, service);

        JsonArray routes = new JsonArray();
        for (Endpoint endpoint : endpoints) {
            JsonObject route = new JsonObject();
            route.addProperty("method", endpoint.getMethod().name());
            route.addProperty("path", endpoint.getPath());
            routes.add(route);
        }
        responseBody.add("routes", routes);

        response.setStatus(Status.CREATED);
        response.setBody(responseBody);
    }

    private void onDelete(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final String name = request.getPathSegment(1);

        Service service = this.services.get(name);
        if (service != null) {
            for (Endpoint endpoint : service.getEndpoints())
                gateway.getNetworkHandler().unregisterEndpoint(endpoint);
        }

        response.setStatus(Status.NO_CONTENT);
    }

    public @NotNull ObeliskGateway getGateway() {
        return gateway;
    }

    public @Nullable Service getServiceByEndpoint(@NotNull Endpoint endpoint) {
        for (Service service : this.services.values())
            for (Endpoint serviceEndpoint : service.getEndpoints())
                if (serviceEndpoint.equals(endpoint))
                    return service;
        return null;
    }
}
