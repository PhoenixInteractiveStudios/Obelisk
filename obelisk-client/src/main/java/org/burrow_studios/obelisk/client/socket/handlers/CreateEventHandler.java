package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.entities.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.jetbrains.annotations.NotNull;

public class CreateEventHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public CreateEventHandler(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = packet.toJson();
        String type = json.get("type").getAsString();

        JsonObject entityData = json.getAsJsonObject("entity");

        EntityBuilder entityBuilder = this.obelisk.getEntityBuilder();

        switch (type) {
            case User.IDENTIFIER -> {
                UserImpl user = entityBuilder.buildUser(entityData);
                this.obelisk.getUsers().add(user);
                // TODO: fire event
            }
            case Ticket.IDENTIFIER -> {
                TicketImpl ticket = entityBuilder.buildTicket(entityData);
                this.obelisk.getTickets().add(ticket);
                // TODO: fire event
            }
            case Project.IDENTIFIER -> {
                ProjectImpl project = entityBuilder.buildProject(entityData);
                this.obelisk.getProjects().add(project);
                // TODO: fire event
            }
            case DiscordAccount.IDENTIFIER -> {
                DiscordAccountImpl discord = entityBuilder.buildDiscordAccount(entityData);
                this.obelisk.getDiscordAccounts().add(discord);
                // TODO: fire event
            }
            case MinecraftAccount.IDENTIFIER -> {
                MinecraftAccountImpl minecraft = entityBuilder.buildMinecraftAccount(entityData);
                this.obelisk.getMinecraftAccounts().add(minecraft);
                // TODO: fire event
            }
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
