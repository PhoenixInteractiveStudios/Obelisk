package org.burrow_studios.obelisk.monolith.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CacheWorker extends Thread {
    private final ObeliskMonolith obelisk;
    private final Connection connection;

    private final ArrayList<JsonObject> buffer = new ArrayList<>();

    public CacheWorker(@NotNull ObeliskMonolith obelisk, @NotNull Connection connection) {
        this.obelisk = obelisk;
        this.connection = connection;

        this.setDaemon(true);
    }

    @Override
    public void run() {
        this.obelisk.getUsers().forEach(user -> buffer.add(user.toJson()));
        this.flush(User.IDENTIFIER);

        this.obelisk.getTickets().forEach(ticket -> buffer.add(ticket.toJson()));
        this.flush(Ticket.IDENTIFIER);

        this.obelisk.getProjects().forEach(project -> buffer.add(project.toJson()));
        this.flush(Project.IDENTIFIER);

        this.obelisk.getDiscordAccounts().forEach(discord -> buffer.add(discord.toJson()));
        this.flush(DiscordAccount.IDENTIFIER);

        this.obelisk.getMinecraftAccounts().forEach(minecraft -> buffer.add(minecraft.toJson()));
        this.flush(MinecraftAccount.IDENTIFIER);
    }

    private void flush(@NotNull String type) {
        for (JsonObject entity : buffer) {
            JsonObject json = new JsonObject();
            json.addProperty("type", type);
            json.add("data", entity);

            Packet packet = new Packet(Opcode.ENTITY_DATA, json);
            this.connection.send(packet);
        }
    }
}
