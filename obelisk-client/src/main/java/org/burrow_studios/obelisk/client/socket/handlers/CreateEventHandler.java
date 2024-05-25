package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.event.EventManager;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserCreateEvent;
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
        long eventId = json.get("id").getAsLong();

        JsonObject entityData = json.getAsJsonObject("entity");

        EntityBuilder entityBuilder = this.obelisk.getEntityBuilder();
        EventManager  eventManager  = this.obelisk.getEventManager();

        switch (type) {
            case User.IDENTIFIER -> {
                UserImpl user = entityBuilder.buildUser(entityData);
                this.obelisk.getUsers().add(user);
                eventManager.handle(new UserCreateEvent(eventId, user));
            }
            case Ticket.IDENTIFIER -> {
                TicketImpl ticket = entityBuilder.buildTicket(entityData);
                this.obelisk.getTickets().add(ticket);
                eventManager.handle(new TicketCreateEvent(eventId, ticket));
            }
            case Project.IDENTIFIER -> {
                ProjectImpl project = entityBuilder.buildProject(entityData);
                this.obelisk.getProjects().add(project);
                eventManager.handle(new ProjectCreateEvent(eventId, project));
            }
            case DiscordAccount.IDENTIFIER -> {
                DiscordAccountImpl discord = entityBuilder.buildDiscordAccount(entityData);
                this.obelisk.getDiscordAccounts().add(discord);
                eventManager.handle(new DiscordAccountCreateEvent(eventId, discord));
            }
            case MinecraftAccount.IDENTIFIER -> {
                MinecraftAccountImpl minecraft = entityBuilder.buildMinecraftAccount(entityData);
                this.obelisk.getMinecraftAccounts().add(minecraft);
                eventManager.handle(new MinecraftAccountCreateEvent(eventId, minecraft));
            }
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
