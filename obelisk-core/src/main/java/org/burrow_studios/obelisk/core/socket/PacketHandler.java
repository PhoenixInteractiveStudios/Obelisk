package org.burrow_studios.obelisk.core.socket;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PacketHandler {
    PacketHandler EMPTY = (con, pck) -> { };

    void handle(@NotNull Connection connection, @NotNull Packet packet);
}
