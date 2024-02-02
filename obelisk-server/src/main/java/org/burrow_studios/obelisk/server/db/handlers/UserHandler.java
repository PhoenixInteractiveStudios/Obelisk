package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.db.RequestHandler;
import org.burrow_studios.obelisk.server.db.entity.UserDB;
import org.burrow_studios.obelisk.commons.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UserHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public UserHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final JsonArray users = new JsonArray();
        final Set<Long> userIds;

        try {
            userIds = this.getDB().getUserIds();
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long userId : userIds)
            users.add(userId);

        return request.respond(200, users);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long userId = (long) request.getEndpoint().args()[1];
        final JsonObject user;

        try {
            user = this.getDB().getUser(userId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, user);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);

        final UserImpl user = new UserImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createUser(
                user.getId(),
                user.getName()
        );

        for (long discordId : user.getDiscordIds())
            this.getDB().addUserDiscordId(user.getId(), discordId);

        for (UUID minecraftId : user.getMinecraftIds())
            this.getDB().addUserMinecraftId(user.getId(), minecraftId);

        return request.respond(200, user.toJson());
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long userId = (long) request.getEndpoint().args()[1];

        this.getDB().deleteUser(userId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long userId = (long) request.getEndpoint().args()[1];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(name -> getDB().updateUserName(userId, name));

        final JsonArray discord = getDB().getUser(userId).getAsJsonArray("discord");
        Optional.ofNullable(json.get("discord"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(elements -> {
                    // add new ids
                    for (JsonElement element : elements)
                        if (!discord.contains(element))
                            getDB().addUserDiscordId(userId, element.getAsLong());
                    // remove old ids
                    for (JsonElement element : discord)
                        if (!elements.contains(element))
                            getDB().removeUserDiscordId(userId, element.getAsLong());
                });

        final JsonArray minecraft = getDB().getUser(userId).getAsJsonArray("minecraft");
        Optional.ofNullable(json.get("minecraft"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(elements -> {
                    // add new ids
                    for (JsonElement element : elements)
                        if (!minecraft.contains(element))
                            getDB().addUserMinecraftId(userId, UUID.fromString(element.getAsString()));
                    // remove old ids
                    for (JsonElement element : minecraft)
                        if (!elements.contains(element))
                            getDB().removeUserMinecraftId(userId, UUID.fromString(element.getAsString()));
                });

        final JsonObject user = getDB().getUser(userId);
        return request.respond(200, user);
    }

    private @NotNull UserDB getDB() {
        return this.provider.getUserDB();
    }
}
