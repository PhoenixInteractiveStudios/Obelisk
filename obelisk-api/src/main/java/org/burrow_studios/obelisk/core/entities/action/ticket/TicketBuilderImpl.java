package org.burrow_studios.obelisk.core.entities.action.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.checks.TicketChecks;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.commons.rpc.Endpoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketBuilderImpl extends BuilderImpl<Ticket> implements TicketBuilder {
    public TicketBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Ticket.class,
                Endpoints.Ticket.CREATE.builder().getPath(),
                TicketImpl::new
        );
    }

    @Override
    public @NotNull TicketBuilderImpl setTitle(@Nullable String title) throws IllegalArgumentException {
        TicketChecks.checkTitle(title);
        data.set("title", (title == null)
                ? JsonNull.INSTANCE
                : new JsonPrimitive(title)
        );
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl setState(@NotNull Ticket.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        data.addToArray("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        data.addToArray("users", arr);
        return this;
    }
}
