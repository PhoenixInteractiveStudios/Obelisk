package org.burrow_studios.obelisk.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.DeleteActionImpl;
import org.burrow_studios.obelisk.internal.action.entity.user.UserModifierImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class UserImpl extends TurtleImpl<User> implements User {
    private @NotNull String name;
    private final @NotNull List<Long> discordIds;
    private final @NotNull List<UUID> minecraftIds;

    public UserImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String name,
            @NotNull List<Long> discordIds,
            @NotNull List<UUID> minecraftIds
    ) {
        super(api, id);
        this.name = name;
        this.discordIds = discordIds;
        this.minecraftIds = minecraftIds;
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("name", name);

        JsonArray discordJson = new JsonArray();
        for (long discordId : this.discordIds)
            discordJson.add(discordId);
        json.add("discord", discordJson);

        JsonArray minecraftJson = new JsonArray();
        for (UUID minecraftId : this.minecraftIds)
            minecraftJson.add(minecraftId.toString());
        json.add("minecraft", minecraftJson);

        return json;
    }

    @Override
    public @NotNull UserModifierImpl modify() {
        return new UserModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<User> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                User.class,
                this.getId(),
                Route.User.DELETE.builder()
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull List<Long> getDiscordIds() {
        return List.copyOf(this.discordIds);
    }

    public @NotNull List<Long> getDiscordIdsMutable() {
        return this.discordIds;
    }

    @Override
    public @NotNull List<UUID> getMinecraftIds() {
        return List.copyOf(this.minecraftIds);
    }

    public @NotNull List<UUID> getMinecraftIdsMutable() {
        return this.minecraftIds;
    }
}
