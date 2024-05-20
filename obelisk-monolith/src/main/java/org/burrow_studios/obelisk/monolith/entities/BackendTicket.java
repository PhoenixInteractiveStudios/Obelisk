package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketModifier;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketUserAddAction;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketUserRemoveAction;
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
    public @NotNull DatabaseTicketModifier modify() {
        return new DatabaseTicketModifier(this);
    }

    @Override
    public @NotNull DatabaseTicketDeleteAction delete() {
        return new DatabaseTicketDeleteAction(this);
    }

    @Override
    public @NotNull DatabaseTicketUserAddAction addUser(@NotNull User user) {
        return new DatabaseTicketUserAddAction(this, user);
    }

    @Override
    public @NotNull DatabaseTicketUserRemoveAction removeUser(@NotNull User user) {
        return new DatabaseTicketUserRemoveAction(this, user);
    }
}
