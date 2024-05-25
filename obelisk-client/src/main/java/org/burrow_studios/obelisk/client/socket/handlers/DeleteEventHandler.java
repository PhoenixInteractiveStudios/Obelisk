package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.*;
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

        JsonObject entityData = json.getAsJsonObject("entity");

        EntityBuilder entityBuilder = this.obelisk.getEntityBuilder();

        switch (type) {
            case User.IDENTIFIER -> {
                AbstractUser user = entityBuilder.provideUser(entityData);
                this.obelisk.getUsers().remove(user);
                // TODO: fire event
            }
            case Ticket.IDENTIFIER -> {
                AbstractTicket ticket = entityBuilder.provideTicket(entityData);
                this.obelisk.getTickets().remove(ticket);
                // TODO: fire event
            }
            case Project.IDENTIFIER -> {
                AbstractProject project = entityBuilder.provideProject(entityData);
                this.obelisk.getProjects().remove(project);
                // TODO: fire event
            }
            case DiscordAccount.IDENTIFIER -> {
                AbstractDiscordAccount discord = entityBuilder.provideDiscordAccount(entityData);
                this.obelisk.getDiscordAccounts().remove(discord);
                // TODO: fire event
            }
            case MinecraftAccount.IDENTIFIER -> {
                AbstractMinecraftAccount minecraft = entityBuilder.provideMinecraftAccount(entityData);
                this.obelisk.getMinecraftAccounts().remove(minecraft);
                // TODO: fire event
            }
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}
