package org.burrow_studios.obelisk.monolith.socket;

import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.jetbrains.annotations.NotNull;

public class SocketHandler {
    public void onDisconnect(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO
    }

    public void onHello(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }

    public void onIdentify(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO
    }

    public void onHeartbeat(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: respond
    }

    public void onHeartbeatAck(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }

    public void onCreateEvent(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }

    public void onDeleteEvent(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }

    public void onUpdateEvent(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }

    public void onCacheRequest(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO
    }

    public void onEntityData(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }

    public void onCacheDone(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: fail & disconnect
    }
}
