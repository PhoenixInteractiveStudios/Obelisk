package org.burrow_studios.obelisk.server.net.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.common.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

/** A piece of outgoing data */
public class Packet {
    private final long id;
    private final long time;
    private final @NotNull JsonObject json;

    public Packet(@NotNull JsonObject json) {
        this.json = json;

        try {
            this.id = json.get("id").getAsLong();
            this.time = TurtleGenerator.getTime(this.id);
        } catch (UnsupportedOperationException | NumberFormatException | IllegalStateException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public @NotNull JsonObject getJson() {
        return json;
    }
}
