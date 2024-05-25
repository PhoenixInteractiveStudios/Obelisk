package org.burrow_studios.obelisk.client.socket.handlers;

import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.jetbrains.annotations.NotNull;

public class DisconnectHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public DisconnectHandler(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: crash
    }
}
