package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MinecraftAccountHandler {
    private final ObeliskMonolith obelisk;

    public MinecraftAccountHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onList(@NotNull Request request) throws RequestHandlerException {
        JsonArray arr = new JsonArray();

        this.obelisk.getMinecraftAccounts().forEach(acc -> arr.add(acc.toMinimalJson()));

        return new Response.Builder()
                .setStatus(200)
                .setBody(arr)
                .build();
    }

    public @NotNull Response onGet(@NotNull Request request) throws RequestHandlerException {
        final long minecraftAccountId = request.parsePathSegment(2, Long::parseLong);

        AbstractMinecraftAccount minecraftAccount = this.obelisk.getMinecraftAccount(minecraftAccountId);

        if (minecraftAccount == null) {
            // TODO: query database?
        }

        if (minecraftAccount == null)
            throw new NotFoundException("Minecraft account not found");

        return new Response.Builder()
                .setBody(minecraftAccount.toJson())
                .setStatus(200)
                .build();
    }

    public @NotNull Response onPost(@NotNull Request request) throws RequestHandlerException {
        JsonObject requestJson = request.requireBodyObject();

        DatabaseMinecraftAccountBuilder builder = this.obelisk.createMinecraftAccount();

        UUID uuid = Pipe.of(requestJson.get("uuid"), BadRequestException::new)
                .nonNull("Missing \"uuid\" attribute")
                .map(JsonElement::getAsString, "Malformed \"uuid\" attribute")
                .map(UUID::fromString, "Malformed \"uuid\" attribute")
                .get();
        builder.setUUID(uuid);

        String name = Pipe.of(requestJson.get("name"), BadRequestException::new)
                .nonNull("Missing \"name\" attribute")
                .map(JsonElement::getAsString, "Malformed \"name\" attribute")
                .get();
        builder.setCachedName(name);

        Long userId = Pipe.of(requestJson.get("user"), BadRequestException::new)
                .map(json -> {
                    if (json == null || json.isJsonNull())
                        return null;
                    return json.getAsLong();
                }, "Malformed \"user\" attribute")
                .get();
        if (userId != null) {
            AbstractUser user = this.obelisk.getUser(userId);

            if (user == null)
                throw new NotFoundException("User not found");

            builder.setUser(user);
        }

        AbstractMinecraftAccount minecraftAccount;
        try {
            minecraftAccount = ((AbstractMinecraftAccount) builder.await());
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(minecraftAccount.toJson())
                .setStatus(201)
                .build();
    }

    public @NotNull Response onDelete(@NotNull Request request) throws RequestHandlerException {
        final long minecraftAccountId = request.parsePathSegment(1, Long::parseLong);

        MinecraftAccount minecraftAccount = this.obelisk.getMinecraftAccount(minecraftAccountId);

        if (minecraftAccount != null)
            try {
                minecraftAccount.delete().await();
            } catch (ExecutionException | InterruptedException e) {
                throw new InternalServerErrorException();
            }

        return new Response.Builder()
                .setStatus(204)
                .build();
    }
}
