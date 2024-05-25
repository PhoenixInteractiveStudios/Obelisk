package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.event.EventManager;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserDeleteEvent;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.jetbrains.annotations.NotNull;

public class DeleteEventHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public DeleteEventHandler(@NotNull ObeliskImpl obelisk) {
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
                AbstractUser user = entityBuilder.provideUser(entityData);
                this.obelisk.getUsers().remove(user);
                eventManager.handle(new UserDeleteEvent(eventId, user));
            }
            case Ticket.IDENTIFIER -> {
                AbstractTicket ticket = entityBuilder.provideTicket(entityData);
                this.obelisk.getTickets().remove(ticket);
                eventManager.handle(new TicketDeleteEvent(eventId, ticket));
            }
            case Project.IDENTIFIER -> {
                AbstractProject project = entityBuilder.provideProject(entityData);
                this.obelisk.getProjects().remove(project);
                eventManager.handle(new ProjectDeleteEvent(eventId, project));
            }
            case DiscordAccount.IDENTIFIER -> {
                AbstractDiscordAccount discord = entityBuilder.provideDiscordAccount(entityData);
                this.obelisk.getDiscordAccounts().remove(discord);
                eventManager.handle(new DiscordAccountDeleteEvent(eventId, discord));
            }
            case MinecraftAccount.IDENTIFIER -> {
                AbstractMinecraftAccount minecraft = entityBuilder.provideMinecraftAccount(entityData);
                this.obelisk.getMinecraftAccounts().remove(minecraft);
                eventManager.handle(new MinecraftAccountDeleteEvent(eventId, minecraft));
            }
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
