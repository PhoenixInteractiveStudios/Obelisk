package org.burrow_studios.obelisk.commons.rpc.amqp;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

class AMQPConnections {
    private static final ConcurrentHashMap<Client, ConnectionFactory> factories = new ConcurrentHashMap<>();

    private record Client(
            @NotNull String host,
            int port,
            @NotNull String user,
            int passHash
    ) { }

    public static Connection getConnection(@NotNull String host, int port, @NotNull String user, @NotNull String pass) throws IOException, TimeoutException {
        Client client = new Client(host, port, user, pass.hashCode());
        ConnectionFactory factory = factories.computeIfAbsent(client, client1 -> new ConnectionFactory());
        return factory.newConnection();
    }
}
