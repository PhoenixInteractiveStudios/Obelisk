package org.burrow_studios.obelisk.core.entities.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.DeleteActionImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.action.user.UserModifierImpl;
import org.burrow_studios.obelisk.commons.http.Endpoints;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildList;

public final class UserImpl extends TurtleImpl implements User {
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

    public UserImpl(@NotNull ObeliskImpl api, @NotNull EntityData data) {
        super(api, data.getId());

        final JsonObject json = data.toJson();

        this.name = json.get("name").getAsString();

        this.discordIds   = buildList(json, "discord", JsonElement::getAsLong);
        this.minecraftIds = buildList(json, "minecraft", e -> UUID.fromString(e.getAsString()));
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
                Endpoints.User.DELETE.builder()
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
