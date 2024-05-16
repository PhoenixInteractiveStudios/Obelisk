package org.burrow_studios.obelisk.client.action.entity.ticket;

import com.google.gson.JsonNull;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.client.EntityBuilder;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.BuilderImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketBuilderImpl extends BuilderImpl<Ticket> implements TicketBuilder {
    public TicketBuilderImpl(@NotNull ObeliskImpl obelisk) {
        super(obelisk, createRoute(), EntityBuilder::buildTicket);
    }

    private static Route.Compiled createRoute() {
        return Route.Ticket.CREATE_TICKET.compile();
    }

    @Override
    public @NotNull TicketBuilderImpl setTitle(@Nullable String title) {
        if (title == null)
            this.data.add("title", JsonNull.INSTANCE);
        else
            this.data.addProperty("title", title);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl setState(@NotNull Ticket.State state) {
        this.data.addProperty("state", state.name());
        return this;
    }
}
