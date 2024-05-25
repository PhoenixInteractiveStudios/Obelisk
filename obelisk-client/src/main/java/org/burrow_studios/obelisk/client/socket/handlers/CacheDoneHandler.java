package org.burrow_studios.obelisk.client.socket.handlers;

import org.burrow_studios.obelisk.api.Status;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.jetbrains.annotations.NotNull;

public class CacheDoneHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public CacheDoneHandler(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        this.obelisk.setStatus(Status.READY);
    }
}
