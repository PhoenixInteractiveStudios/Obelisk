package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserBuilder;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.util.Pipe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class UserHandler {
    private final ObeliskMonolith obelisk;

    public UserHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public @NotNull Response onList(@NotNull Request request) throws RequestHandlerException {
        JsonArray arr = new JsonArray();

        this.obelisk.getUsers().forEach(users -> arr.add(users.toMinimalJson()));

        return new Response.Builder()
                .setStatus(200)
                .setBody(arr)
                .build();
    }

    public @NotNull Response onGet(@NotNull Request request) throws RequestHandlerException {
        final long userId = request.parsePathSegment(1, Long::parseLong);

        AbstractUser user = this.obelisk.getUser(userId);

        if (user == null) {
            // TODO: query database?
        }

        if (user == null)
            throw new NotFoundException("User not found");

        return new Response.Builder()
                .setBody(user.toJson())
                .setStatus(200)
                .build();
    }

    public @NotNull Response onPost(@NotNull Request request) throws RequestHandlerException {
        JsonObject requestJson = request.requireBodyObject();

        DatabaseUserBuilder builder = this.obelisk.createUser();

        String name = Pipe.of(requestJson.get("name"), BadRequestException::new)
                .nonNull("Missing \"name\" attribute")
                .map(JsonElement::getAsString, "Malformed \"name\" attribute")
                .get();
        builder.setName(name);

        AbstractUser user;
        try {
            user = ((AbstractUser) builder.await());
        } catch (ExecutionException | InterruptedException e) {
            throw new InternalServerErrorException();
        }

        return new Response.Builder()
                .setBody(user.toJson())
                .setStatus(201)
                .build();
    }

    public @NotNull Response onDelete(@NotNull Request request) throws RequestHandlerException {
        final long userId = request.parsePathSegment(1, Long::parseLong);

        User user = this.obelisk.getUser(userId);

        if (user != null)
            try {
                user.delete().await();
            } catch (ExecutionException | InterruptedException e) {
                throw new InternalServerErrorException();
            }

        return new Response.Builder()
                .setStatus(204)
                .build();
    }
}
