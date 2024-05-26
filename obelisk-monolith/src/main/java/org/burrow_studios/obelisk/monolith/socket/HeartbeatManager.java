package org.burrow_studios.obelisk.monolith.socket;

import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.SocketServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatManager implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatManager.class);

    public static final long HEARTBEAT_INTERVAL = 2000;

    private final SocketServer server;
    private final ConcurrentHashMap<Connection, Long> lastHeartbeats;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public HeartbeatManager(@NotNull SocketServer server) {
        this.server = server;
        this.lastHeartbeats = new ConcurrentHashMap<>();

        this.executor.schedule(this::checkHeartbeats, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
        this.executor.schedule(this::checkGhostConnections, HEARTBEAT_INTERVAL * 10, TimeUnit.MILLISECONDS);
    }

    private void checkHeartbeats() {
        final long now = System.currentTimeMillis();
        final long min = now - (HEARTBEAT_INTERVAL * 2);

        for (Map.Entry<Connection, Long> entry : this.lastHeartbeats.entrySet()) {
            final Connection connection = entry.getKey();
            final long lastHeartbeat = entry.getValue();

            if (lastHeartbeat >= min) continue;

            LOG.warn("A client missed one ore more heartbeats. Disconnecting...");

            try {
                connection.close();
            } catch (IOException e) {
                LOG.warn("Failed to properly disconnect", e);
            }

            this.lastHeartbeats.remove(connection);
        }

        this.executor.schedule(this::checkHeartbeats, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void checkGhostConnections() {
        for (Connection connection : this.server.getConnections()) {
            if (this.lastHeartbeats.containsKey(connection)) continue;

            this.lastHeartbeats.put(connection, System.currentTimeMillis());
        }

        this.executor.schedule(this::checkGhostConnections, HEARTBEAT_INTERVAL * 4, TimeUnit.MILLISECONDS);
    }

    public void onHeartbeat(@NotNull Connection connection) {
        this.lastHeartbeats.put(connection, System.currentTimeMillis());
    }

    @Override
    public void close() {
        this.executor.shutdown();
    }
}
