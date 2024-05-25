package org.burrow_studios.obelisk.core.socket;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.socket.SocketIO.GSON;

@FunctionalInterface
public interface Receiver {
    void receive(@NotNull JsonObject data);

    default void receive(@NotNull String data) {
        this.receive(GSON.fromJson(data, JsonObject.class));
    }

    default void receive(byte[] data) {
        this.receive(new String(data));
    }
}
