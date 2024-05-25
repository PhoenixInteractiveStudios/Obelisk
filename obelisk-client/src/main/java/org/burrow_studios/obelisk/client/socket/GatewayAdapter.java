package org.burrow_studios.obelisk.client.socket;

import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.config.GatewayConfig;
import org.burrow_studios.obelisk.client.socket.handlers.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class GatewayAdapter implements Closeable {
    private final ObeliskImpl obelisk;
    private final GatewayConfig config;
    private Connection connection;

    public GatewayAdapter(@NotNull ObeliskImpl obelisk, @NotNull GatewayConfig config) {
        this.obelisk = obelisk;
        this.config = config;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(config.getHost(), config.getPort());
        this.connection = new Connection(socket);

        this.connection.addHandler(Opcode.DISCONNECT, new DisconnectHandler(obelisk));
        this.connection.addHandler(Opcode.HELLO, new HelloHandler());
        this.connection.addHandler(Opcode.ENCRYPTION, new EncryptionHandler());

        this.connection.addHandler(Opcode.HEARTBEAT_ACK, new HeartbeatHandler());

        this.connection.addHandler(Opcode.CREATE_EVENT, new CreateEventHandler());
        this.connection.addHandler(Opcode.DELETE_EVENT, new DeleteEventHandler());
        this.connection.addHandler(Opcode.UPDATE_EVENT, new UpdateEventHandler());

        this.connection.addHandler(Opcode.ENTITY_DATA, new CacheDataHandler(obelisk));
        this.connection.addHandler(Opcode.CACHE_DONE, new CacheDoneHandler(obelisk));

        UnexpectedHandler unexpectedHandler = new UnexpectedHandler();
        this.connection.addHandler(Opcode.IDENTIFY, unexpectedHandler);
        this.connection.addHandler(Opcode.HEARTBEAT, unexpectedHandler);
        this.connection.addHandler(Opcode.CACHE_REQUEST, unexpectedHandler);
    }

    @Override
    public void close() throws IOException {
        this.connection.close();
    }
}
