package org.burrow_studios.obelisk.core.socket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SocketServer implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(SocketServer.class);

    private final ServerSocket socket;
    private final Thread listener;

    private final ConcurrentHashMap<Opcode, PacketHandler> handlers = new ConcurrentHashMap<>();
    private final Set<Connection> connections = ConcurrentHashMap.newKeySet();

    public SocketServer(int port) throws IOException {
        this.socket = new ServerSocket(port);

        this.listener = new Thread(this::listen);
        this.listener.setDaemon(true);
    }

    public void start() {
        this.listener.start();
    }

    @Override
    public void close() throws IOException {
        this.listener.interrupt();
        for (Connection connection : this.connections)
            connection.close();
        this.socket.close();
    }

    private void listen() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = this.socket.accept();

                Connection connection = new Connection(client, this);

                this.connections.add(connection);
            } catch (IOException e) {
                LOG.warn("Could not accept client connection due to an IOException", e);
            } catch (Throwable t) {
                LOG.warn("Uncaught Throwable in listener thread!", t);
            }
        }
    }

    public void addHandler(@NotNull Opcode opcode, @NotNull PacketHandler handler) {
        this.handlers.put(opcode, handler);
    }

    public Map<Opcode, PacketHandler> getHandlers() {
        return Collections.unmodifiableMap(this.handlers);
    }
}
