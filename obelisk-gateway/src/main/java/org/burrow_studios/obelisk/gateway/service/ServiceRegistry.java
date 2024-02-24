package org.burrow_studios.obelisk.gateway.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPClient;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPServer;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.rpc.http.HTTPClient;
import org.burrow_studios.obelisk.commons.rpc.http.SunServerImpl;
import org.burrow_studios.obelisk.commons.util.validation.Validation;
import org.burrow_studios.obelisk.commons.yaml.YamlPrimitive;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.Main;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class ServiceRegistry implements Closeable {
    private static final File CONFIG_DIR = new File(Main.DIR, "services/");

    private static final String LEGAL_NAME_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String LEGAL_NAME_DELIMITERS = "-._";

    private final YamlSection config;
    private final RPCServer<?> server;

    private final Set<Service> services;

    public ServiceRegistry(@NotNull YamlSection config) throws IOException, TimeoutException {
        this.config = config;

        this.services = ConcurrentHashMap.newKeySet();

        YamlSection serverConfig = config.getAsSection("server");
        YamlPrimitive type = serverConfig.getAsPrimitive("type");

        this.server = switch (type.getAsString().toLowerCase()) {
            case "amqp" -> new AMQPServer(
                    serverConfig.getAsPrimitive("host").getAsString(),
                    serverConfig.getAsPrimitive("port").getAsInt(),
                    serverConfig.getAsPrimitive("user").getAsString(),
                    serverConfig.getAsPrimitive("pass").getAsString(),
                    serverConfig.getAsPrimitive("exchange").getAsString(),
                    serverConfig.getAsPrimitive("queue").getAsString()
            );
            case "http" -> new SunServerImpl(
                    serverConfig.getAsPrimitive("port").getAsInt()
            );
            default -> throw new IllegalArgumentException("Unsupported server type: " + type.getAsString());
        };

        this.server.addEndpoint(Endpoint.build(Method.POST  , "/services"        ), this::onPost);
        this.server.addEndpoint(Endpoint.build(Method.DELETE, "/services/:string"), this::onDelete);
    }

    @Override
    public void close() throws IOException {
        this.server.close();
    }

    private void onPost(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        if (!(requestBody.get("name") instanceof JsonPrimitive nameInfo))
            throw new BadRequestException("Malformed body: missing name");
        final String name = nameInfo.getAsString();

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

        if (!(requestBody.get("connection") instanceof JsonObject connectionInfo))
            throw new BadRequestException("Malformed body: Missing connection info");

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

        if (proxyClient == null) {
            // TODO: fail
            throw new InternalServerErrorException();
        }

        if (!(requestBody.get("routes") instanceof JsonArray routesInfo))
            throw new BadRequestException("Malformed body: Missing routing info");

        Set<Endpoint> endpoints = new LinkedHashSet<>();

        // TODO: add auth specs
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

            Endpoint endpoint = Endpoint.build(method, path);

            for (Service service : this.services) {
                for (Endpoint serviceEndpoint : service.getEndpoints()) {
                    if (!serviceEndpoint.equals(endpoint)) continue;
                    // CONFLICT! The existing route will be respected
                    continue routes;
                }
            }

            endpoints.add(endpoint);
        }

        final Service service = new Service(this, proxyClient, name, endpoints);

        this.services.add(service);

        // TODO: register endpoints with exposed RPCServer
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
        final String name = request.getPath().split("/")[1];

        for (Service service : this.services) {
            if (!service.getName().equals(name)) continue;

            // TODO: remove routes
        }

        response.setStatus(Status.NO_CONTENT);
    }
}
