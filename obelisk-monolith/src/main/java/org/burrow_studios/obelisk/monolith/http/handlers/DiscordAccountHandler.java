package org.burrow_studios.obelisk.monolith.http.handlers;

import org.burrow_studios.obelisk.core.entities.AbstractDiscordAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.http.Request;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountHandler {
    private final ObeliskMonolith obelisk;

    public DiscordAccountHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
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
}
