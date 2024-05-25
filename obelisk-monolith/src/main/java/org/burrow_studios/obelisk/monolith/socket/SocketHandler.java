package org.burrow_studios.obelisk.monolith.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.jetbrains.annotations.NotNull;

public class SocketHandler {
    public void onDisconnect(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO
    }

    public void onIdentify(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO
    }

    public void onHeartbeat(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO: respond
    }

    public void onCacheRequest(@NotNull Connection connection, @NotNull Packet packet) {
        // TODO
    }

    public void onUnexpected(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = new JsonObject();
        json.addProperty("reason", "Unexpected packet");
        connection.send(new Packet(Opcode.DISCONNECT, json));
    }
}
