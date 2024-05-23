package org.burrow_studios.obelisk.monolith.http.handlers;

import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

public class MinecraftAccountHandler {
    private final ObeliskMonolith obelisk;

    public MinecraftAccountHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
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
}
