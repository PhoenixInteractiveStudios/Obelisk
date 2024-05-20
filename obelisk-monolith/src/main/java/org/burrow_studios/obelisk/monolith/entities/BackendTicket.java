package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackendTicket extends AbstractTicket {
    public BackendTicket(
            @NotNull ObeliskMonolith obelisk,
            long id,
            @Nullable String title,
            @NotNull State state,
            @NotNull OrderedEntitySetView<AbstractUser> users
    ) {
        super(obelisk, id, title, state, users);
    }

    @Override
    public @NotNull TicketModifier modify() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DeleteAction<Ticket> delete() {
        // TODO
        return null;
    }

    @Override
    public @NotNull Action<Void> addUser(@NotNull User user) {
        // TODO
        return null;
    }

    @Override
    public @NotNull Action<Void> removeUser(@NotNull User user) {
        // TODO
        return null;
    }
}
