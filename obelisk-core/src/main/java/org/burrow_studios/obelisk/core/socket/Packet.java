package org.burrow_studios.obelisk.core.socket;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class Packet {
    private final Opcode opcode;
    private final JsonObject data;

    public Packet(@NotNull Opcode opcode, @NotNull JsonObject data) {
        this.opcode = opcode;

        this.data = new JsonObject();
        this.data.addProperty("op", opcode.getCode());
        data.asMap().forEach((s, jsonElement) -> {
            if (s.equals("op"))
                return;
            this.data.add(s, jsonElement);
        });
    }

    public Packet(@NotNull Opcode opcode) {
        this.opcode = opcode;
        this.data = new JsonObject();
        this.data.addProperty("op", opcode.getCode());
    }

    public @NotNull Opcode getOpcode() {
        return this.opcode;
    }

    public @NotNull JsonObject toJson() {
        return this.data;
    }
}
