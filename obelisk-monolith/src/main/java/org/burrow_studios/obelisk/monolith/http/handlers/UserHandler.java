package org.burrow_studios.obelisk.monolith.http.handlers;

import com.google.gson.JsonArray;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

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
}
