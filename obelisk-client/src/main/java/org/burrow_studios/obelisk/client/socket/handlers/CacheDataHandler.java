package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.entities.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.jetbrains.annotations.NotNull;

public class CacheDataHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public CacheDataHandler(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = packet.toJson();
        String type = json.get("type").getAsString();

        JsonObject data = json.getAsJsonObject("data");

        EntityBuilder entityBuilder = this.obelisk.getEntityBuilder();

        switch (type) {
            case "user" -> {
                UserImpl user = entityBuilder.buildUser(data);
                this.obelisk.getUsers().add(user);
            }
            case "ticket" -> {
                TicketImpl ticket = entityBuilder.buildTicket(data);
                this.obelisk.getTickets().add(ticket);
            }
            case "project" -> {
                ProjectImpl project = entityBuilder.buildProject(data);
                this.obelisk.getProjects().add(project);
            }
            case "discord" -> {
                DiscordAccountImpl discord = entityBuilder.buildDiscordAccount(data);
                this.obelisk.getDiscordAccounts().add(discord);
            }
            case "minecraft" -> {
                MinecraftAccountImpl minecraft = entityBuilder.buildMinecraftAccount(data);
                this.obelisk.getMinecraftAccounts().add(minecraft);
            }
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
