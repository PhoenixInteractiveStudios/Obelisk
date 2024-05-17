package org.burrow_studios.obelisk.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.client.entities.*;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityBuilder {
    private final ObeliskImpl obelisk;

    public EntityBuilder(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull UserImpl buildUser(@NotNull JsonObject data) {
        final long   id   = data.get("id").getAsLong();
        final String name = data.get("name").getAsString();

        JsonArray discordArray = data.getAsJsonArray("discord");
        OrderedEntitySetView<AbstractDiscordAccount> discordAccounts = new OrderedEntitySetView<>(obelisk.getDiscordAccounts(), AbstractDiscordAccount.class);
        for (JsonElement element : discordArray) {
            JsonObject discordJson = element.getAsJsonObject();
            // FIXME: the JSON won't contain any reference to the user; should be passed explicitly
            AbstractDiscordAccount discordAccount = this.provideDiscordAccount(discordJson);
            discordAccounts.add(discordAccount);
        }

        JsonArray minecraftArray = data.getAsJsonArray("minecraft");
        OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts = new OrderedEntitySetView<>(obelisk.getMinecraftAccounts(), AbstractMinecraftAccount.class);
        for (JsonElement element : minecraftArray) {
            JsonObject minecraftJson = element.getAsJsonObject();
            // FIXME: the JSON won't contain any reference to the user; should be passed explicitly
            AbstractMinecraftAccount minecraftAccount = this.provideMinecraftAccount(minecraftJson);
            minecraftAccounts.add(minecraftAccount);
        }

        return new UserImpl(obelisk, id, name, discordAccounts, minecraftAccounts);
    }

    public @NotNull AbstractUser provideUser(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        AbstractUser user = obelisk.getUser(id);

        if (user != null) {
            // TODO: implicit updates?
            return user;
        } else {
            user = this.buildUser(data);
            obelisk.getUsers().add(user);
        }

        return user;
    }

    public @NotNull TicketImpl buildTicket(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        JsonElement titleElement = data.get("title");
        String title = titleElement.isJsonNull() ? null : titleElement.getAsString();

        String stateStr = data.get("state").getAsString();
        Ticket.State state = Ticket.State.valueOf(stateStr);

        JsonArray userArray = data.getAsJsonArray("users");
        OrderedEntitySetView<AbstractUser> users = new OrderedEntitySetView<>(obelisk.getUsers(), AbstractUser.class);
        for (JsonElement element : userArray) {
            JsonObject userJson = element.getAsJsonObject();
            AbstractUser user = this.provideUser(userJson);
            users.add(user);
        }

        return new TicketImpl(obelisk, id, title, state, users);
    }

    public @NotNull ProjectImpl buildProject(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        String title = data.get("title").getAsString();

        String stateStr = data.get("state").getAsString();
        Project.State state = Project.State.valueOf(stateStr);

        JsonArray userArray = data.getAsJsonArray("members");
        OrderedEntitySetView<AbstractUser> members = new OrderedEntitySetView<>(obelisk.getUsers(), AbstractUser.class);
        for (JsonElement element : userArray) {
            JsonObject userJson = element.getAsJsonObject();
            AbstractUser user = this.provideUser(userJson);
            members.add(user);
        }

        return new ProjectImpl(obelisk, id, title, state, members);
    }

    public @NotNull DiscordAccountImpl buildDiscordAccount(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        long snowflake = data.get("snowflake").getAsLong();

        String cachedName = data.get("name").getAsString();

        JsonObject userJson = data.getAsJsonObject("user");
        AbstractUser user = this.provideUser(userJson);

        DiscordAccountImpl entity = new DiscordAccountImpl(obelisk, id, snowflake, cachedName, user);

        // ensure the user object knows about this entity
        user.getDiscordAccounts().add(entity);

        return entity;
    }

    public @NotNull AbstractDiscordAccount provideDiscordAccount(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        AbstractDiscordAccount discordAccount = obelisk.getDiscordAccount(id);

        if (discordAccount != null) {
            // TODO: implicit updates?
            return discordAccount;
        } else {
            discordAccount = this.buildDiscordAccount(data);
            obelisk.getDiscordAccounts().add(discordAccount);
        }

        return discordAccount;
    }

    public @NotNull MinecraftAccountImpl buildMinecraftAccount(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        String uuidStr = data.get("snowflake").getAsString();
        UUID uuid = UUID.fromString(uuidStr);

        String cachedName = data.get("name").getAsString();

        JsonObject userJson = data.getAsJsonObject("user");
        AbstractUser user = this.provideUser(userJson);

        MinecraftAccountImpl entity = new MinecraftAccountImpl(obelisk, id, uuid, cachedName, user);

        // ensure the user object knows about this entity
        user.getMinecraftAccounts().add(entity);

        return entity;
    }

    public @NotNull AbstractMinecraftAccount provideMinecraftAccount(@NotNull JsonObject data) {
        final long id = data.get("id").getAsLong();

        AbstractMinecraftAccount minecraftAccount = obelisk.getMinecraftAccount(id);

        if (minecraftAccount != null) {
            // TODO: implicit updates?
            return minecraftAccount;
        } else {
            minecraftAccount = this.buildMinecraftAccount(data);
            obelisk.getMinecraftAccounts().add(minecraftAccount);
        }

        return minecraftAccount;
    }
}
