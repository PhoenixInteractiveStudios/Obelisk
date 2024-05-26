package org.burrow_studios.obelisk.client.socket;

import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.config.GatewayConfig;
import org.burrow_studios.obelisk.client.socket.handlers.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GatewayAdapter implements Closeable {
    private final ObeliskImpl obelisk;
    private final GatewayConfig config;
    private Connection connection;

    private long heartbeatInterval = 2000;

    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

    public GatewayAdapter(@NotNull ObeliskImpl obelisk, @NotNull GatewayConfig config) {
        this.obelisk = obelisk;
        this.config = config;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(config.getHost(), config.getPort());
        this.connection = new Connection(socket);

        this.connection.addHandler(Opcode.DISCONNECT, new DisconnectHandler(obelisk));
        this.connection.addHandler(Opcode.HELLO, new HelloHandler(obelisk));
        this.connection.addHandler(Opcode.ENCRYPTION, new EncryptionHandler(this));

        this.connection.addHandler(Opcode.HEARTBEAT_ACK, new HeartbeatHandler());

        this.connection.addHandler(Opcode.CREATE_EVENT, new CreateEventHandler(obelisk));
        this.connection.addHandler(Opcode.DELETE_EVENT, new DeleteEventHandler(obelisk));
        this.connection.addHandler(Opcode.UPDATE_EVENT, new UpdateEventHandler(obelisk));

        this.connection.addHandler(Opcode.ENTITY_DATA, new CacheDataHandler(obelisk));
        this.connection.addHandler(Opcode.CACHE_DONE, new CacheDoneHandler(obelisk));

        UnexpectedHandler unexpectedHandler = new UnexpectedHandler();
        this.connection.addHandler(Opcode.IDENTIFY, unexpectedHandler);
        this.connection.addHandler(Opcode.HEARTBEAT, unexpectedHandler);
        this.connection.addHandler(Opcode.CACHE_REQUEST, unexpectedHandler);
    }

    public void initHeartbeats(long interval) {
        this.heartbeatInterval = interval;

        this.heartbeatExecutor.schedule(this::heartbeat, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    private void heartbeat() {
        if (this.connection.isClosed()) return;

        Packet packet = new Packet(Opcode.HEARTBEAT);
        this.connection.send(packet);

        this.heartbeatExecutor.schedule(this::heartbeat, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() throws IOException {
        this.connection.close();
    }
}
