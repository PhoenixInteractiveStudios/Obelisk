package org.burrow_studios.obelisk.server.net.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.http.server.HTTPRequest;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.burrow_studios.obelisk.commons.http.server.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.NotFoundException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.action.user.UserBuilderImpl;
import org.burrow_studios.obelisk.core.entities.action.user.UserModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class UserHandler {
    private final @NotNull NetworkHandler networkHandler;

    public UserHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        JsonArray json = new JsonArray();
        for (long id : getAPI().getUsers().getIdsAsImmutaleSet())
            json.add(id);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long userId = request.getSegmentLong(1);

        final UserImpl user = getAPI().getUser(userId);
        if (user == null)
            throw new NotFoundException();

        response.setCode(200);
        response.setBody(user.toJson());
    }

    public void onCreate(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        final UserBuilderImpl builder;

        try {
            final String name = json.get("name").getAsString();

            builder = getAPI().createUser()
                    .setName(name);

            JsonArray discord = json.getAsJsonArray("discord");
            StreamSupport.stream(discord.spliterator(), false)
                    .map(JsonElement::getAsLong)
                    .forEach(builder::addDiscordIds);

            JsonArray minecraft = json.getAsJsonArray("minecraft");
            StreamSupport.stream(minecraft.spliterator(), false)
                    .map(JsonElement::getAsString)
                    .map(UUID::fromString)
                    .forEach(builder::addMinecraftIds);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            result = ((UserImpl) builder.await()).toJson();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onDelete(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long userId = request.getSegmentLong(1);
        final UserImpl user = getAPI().getUser(userId);

        if (user != null) {
            try {
                user.delete().await();
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }

        response.setCode(204);
    }

    public void onEdit(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long userId = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();

        UserImpl user = getAPI().getUser(userId);
        if (user == null)
            throw new NotFoundException();
        final UserModifierImpl modifier = user.modify();

        try {
            Optional.ofNullable(json.get("name"))
                    .map(JsonElement::getAsString)
                    .ifPresent(modifier::setName);

            final List<Long> discord = user.getDiscordIds();
            Optional.ofNullable(json.get("discord"))
                    .map(JsonElement::getAsJsonArray)
                    .map(elements -> {
                        List<Long> snowflakes = new ArrayList<>(elements.size());
                        for (JsonElement element : elements)
                            snowflakes.add(element.getAsLong());
                        return snowflakes;
                    })
                    .ifPresent(elements -> {
                        // add new ids
                        for (long element : elements)
                            if (!discord.contains(element))
                                modifier.addDiscordIds(element);
                        // remove old ids
                        for (long element : discord)
                            if (!elements.contains(element))
                                modifier.removeDiscordIds(element);
                    });

            final List<UUID> minecraft = user.getMinecraftIds();
            Optional.ofNullable(json.get("minecraft"))
                    .map(JsonElement::getAsJsonArray)
                    .map(elements -> {
                        List<UUID> uuids = new ArrayList<>(elements.size());
                        for (JsonElement element : elements)
                            uuids.add(UUID.fromString(element.getAsString()));
                        return uuids;
                    })
                    .ifPresent(elements -> {
                        // add new ids
                        for (UUID element : elements)
                            if (!minecraft.contains(element))
                                modifier.addMinecraftIds(element);
                        // remove old ids
                        for (UUID element : minecraft)
                            if (!elements.contains(element))
                                modifier.removeMinecraftIds(element);
                    });
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Malformed body");
        }

        try {
            user = ((UserImpl) modifier.await());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(user.toJson());
    }

    private @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getServer().getAPI();
    }
}
