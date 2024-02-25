package org.burrow_studios.obelisk.commons.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.burrow_studios.obelisk.commons.rpc.amqp.AMQPClient;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ServiceHook implements Closeable {
    private final TimeBasedIdGenerator idGenerator = TimeBasedIdGenerator.get();

    private final String host;
    private final int    port;
    private final String user;
    private final String pass;

    private final String serviceName;

    public ServiceHook(@NotNull String host, int port, @NotNull String user, @NotNull String pass, @NotNull String serviceName, @NotNull RPCServer<?> server) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.serviceName = serviceName;

        try (AMQPClient client = getClient()) {
            JsonArray routes = new JsonArray();
            for (Endpoint endpoint : server.getEndpoints()) {
                JsonObject route = new JsonObject();
                route.addProperty("method", endpoint.getMethod().name());
                route.addProperty("path", endpoint.getPath());
                routes.add(route);
            }

            RPCRequest request = new RPCRequest.Builder()
                    .setMethod(Method.POST)
                    .setPath("/services")
                    .setBody(routes)
                    .build(idGenerator.newId());

            client.send(request).get();
        } catch (Exception e) {
            throw new RuntimeException("Could not properly register client", e);
        }
    }

    public ServiceHook(@NotNull YamlSection config, @NotNull String serviceName, @NotNull RPCServer<?> server) {
        this(
                config.getAsPrimitive("host").getAsString(),
                config.getAsPrimitive("port").getAsInt(),
                config.getAsPrimitive("user").getAsString(),
                config.getAsPrimitive("pass").getAsString(),
                serviceName, server
        );
    }

    // TODO: support updates on the backing server (added or removed endpoints)

    @Override
    public void close() throws IOException {
        try (AMQPClient client = getClient()) {
            RPCRequest request = new RPCRequest.Builder()
                    .setMethod(Method.DELETE)
                    .setPath("/services/" + serviceName)
                    .build(idGenerator.newId());
            client.send(request).get();
        } catch (Exception e) {
            throw new IOException("Could not properly unregister service", e);
        }
    }

    private AMQPClient getClient() throws IOException {
        try {
            return new AMQPClient(host, port, user, pass, "meta", "service_discovery");
        } catch (TimeoutException e) {
            throw new IOException(e);
        }
    }
}
