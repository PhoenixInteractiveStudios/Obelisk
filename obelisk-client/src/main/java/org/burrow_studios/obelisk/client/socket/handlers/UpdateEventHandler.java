package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.event.EventManager;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateEvent;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.EntityUpdater;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.*;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpdateEventHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public UpdateEventHandler(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = packet.toJson();
        String type = json.get("type").getAsString();

        JsonObject entityData = json.getAsJsonObject("entity");
        JsonObject updateData = json.getAsJsonObject("updates");

        EntityBuilder entityBuilder = this.obelisk.getEntityBuilder();
        EventManager  eventManager  = this.obelisk.getEventManager();

        switch (type) {
            case User.IDENTIFIER -> {
                AbstractUser user = entityBuilder.provideUser(entityData);
                List<UserUpdateEvent<?>> events = EntityUpdater.updateUser(0, user, entityData, updateData);
                events.forEach(eventManager::handle);
            }
            case Ticket.IDENTIFIER -> {
                AbstractTicket ticket = entityBuilder.provideTicket(entityData);
                List<TicketUpdateEvent<?>> events = EntityUpdater.updateTicket(0, ticket, entityData, updateData);
                events.forEach(eventManager::handle);
            }
            case Project.IDENTIFIER -> {
                AbstractProject project = entityBuilder.provideProject(entityData);
                List<ProjectUpdateEvent<?>> events = EntityUpdater.updateProject(0, project, entityData, updateData);
                events.forEach(eventManager::handle);
            }
            case DiscordAccount.IDENTIFIER -> {
                AbstractDiscordAccount discordAccount = entityBuilder.provideDiscordAccount(entityData);
                List<DiscordAccountUpdateEvent<?>> events = EntityUpdater.updateDiscordAccount(0, discordAccount, entityData, updateData);
                events.forEach(eventManager::handle);
            }
            case MinecraftAccount.IDENTIFIER -> {
                AbstractMinecraftAccount minecraftAccount = entityBuilder.provideMinecraftAccount(entityData);
                List<MinecraftAccountUpdateEvent<?>> events = EntityUpdater.updateMinecraftAccount(0, minecraftAccount, entityData, updateData);
                events.forEach(eventManager::handle);
            }
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
