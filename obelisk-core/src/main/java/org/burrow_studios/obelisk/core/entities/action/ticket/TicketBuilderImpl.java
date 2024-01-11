package org.burrow_studios.obelisk.core.entities.action.ticket;

import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.data.TicketData;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketBuilderImpl extends BuilderImpl<Ticket, TicketImpl, TicketData> implements TicketBuilder {
    public TicketBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Ticket.class,
                Route.Ticket.CREATE.builder().compile(),
                new TicketData(),
                TicketData::new
        );
    }

    @Override
    public @NotNull TicketBuilderImpl setTitle(@Nullable String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl setState(@NotNull Ticket.State state) {
        this.data.setState(state);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addTags(@NotNull String... tags) {
        this.data.addTags(tags);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addUsers(@NotNull User... users) {
        this.data.addUsers(users);
        return this;
    }
}
