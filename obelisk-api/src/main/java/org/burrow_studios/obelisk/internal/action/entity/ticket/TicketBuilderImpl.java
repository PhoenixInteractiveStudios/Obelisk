package org.burrow_studios.obelisk.internal.action.entity.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketBuilderImpl extends BuilderImpl<Ticket> implements TicketBuilder {
    public TicketBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Ticket.class,
                Route.Ticket.CREATE.builder().compile(),
                EntityBuilder::buildTicket
        );
    }

    @Override
    public @NotNull TicketBuilderImpl setTitle(@Nullable String title) {
        this.set("title", (title == null)
                ? JsonNull.INSTANCE
                : new JsonPrimitive(title)
        );
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl setState(@NotNull Ticket.State state) {
        this.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        this.add("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.remove("users", arr);
        return this;
    }
}
