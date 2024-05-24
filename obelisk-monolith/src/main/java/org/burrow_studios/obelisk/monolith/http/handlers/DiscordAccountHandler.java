package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.DiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class DiscordAccountHandler {
    private final ObeliskMonolith obelisk;

    public DiscordAccountHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onList(@NotNull Request request) throws RequestHandlerException {
        JsonArray arr = new JsonArray();

        this.obelisk.getDiscordAccounts().forEach(acc -> arr.add(acc.toMinimalJson()));

        return new Response.Builder()
                .setStatus(200)
                .setBody(arr)
                .build();
    }

    public @NotNull Response onGet(@NotNull Request request) throws RequestHandlerException {
        final long discordAccountId = request.parsePathSegment(2, Long::parseLong);

        AbstractDiscordAccount discordAccount = this.obelisk.getDiscordAccount(discordAccountId);

        if (discordAccount == null) {
            // TODO: query database?
        }

        if (discordAccount == null)
            throw new NotFoundException("Discord account not found");

        return new Response.Builder()
                .setBody(discordAccount.toJson())
                .setStatus(200)
                .build();
    }

    public @NotNull Response onPost(@NotNull Request request) throws RequestHandlerException {
        JsonObject requestJson = request.requireBodyObject();

        DatabaseDiscordAccountBuilder builder = this.obelisk.createDiscordAccount();

        long snowflake = Pipe.of(requestJson.get("snowflake"), BadRequestException::new)
                .nonNull("Missing \"snowflake\" attribute")
                .map(JsonElement::getAsLong, "Malformed \"snowflake\" attribute")
                .get();
        builder.setSnowflake(snowflake);

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

        AbstractDiscordAccount discordAccount;
        try {
            discordAccount = ((AbstractDiscordAccount) builder.await());
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(discordAccount.toJson())
                .setStatus(201)
                .build();
    }

    public @NotNull Response onDelete(@NotNull Request request) throws RequestHandlerException {
        final long discordAccountId = request.parsePathSegment(1, Long::parseLong);

        DiscordAccount discordAccount = this.obelisk.getDiscordAccount(discordAccountId);

        if (discordAccount != null)
            try {
                discordAccount.delete().await();
            } catch (ExecutionException | InterruptedException e) {
                throw new InternalServerErrorException();
            }

        return new Response.Builder()
                .setStatus(204)
                .build();
    }
}
