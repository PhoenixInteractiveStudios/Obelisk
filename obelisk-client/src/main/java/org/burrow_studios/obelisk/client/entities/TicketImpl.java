package org.burrow_studios.obelisk.client.entities;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.ActionImpl;
import org.burrow_studios.obelisk.client.action.DeleteActionImpl;
import org.burrow_studios.obelisk.client.action.entity.ticket.TicketModifierImpl;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractTicket;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketImpl extends AbstractTicket {
    public TicketImpl(
            @NotNull ObeliskImpl obelisk,
            long id,
            @Nullable String title,
            @NotNull State state,
            @NotNull OrderedEntitySetView<AbstractUser> users
    ) {
        super(obelisk, id, title, state, users);
    }

    @Override
    public @NotNull TicketModifierImpl modify() {
        return new TicketModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Ticket> delete() {
        Route.Compiled route = Route.Ticket.DELETE_TICKET.compile(this.id);

        return new DeleteActionImpl<>(((ObeliskImpl) this.getAPI()), route, this.getId(), Ticket.class);
    }

    @Override
    public @NotNull Action<Void> addUser(@NotNull User user) {
        Route.Compiled route = Route.Ticket.ADD_USER.compile(this.id, user.getId());

        return new ActionImpl<>(this.getAPI(), route) {
            @Override
            protected @Nullable JsonElement getRequestBody() {
                return null;
            }
        };
    }

    @Override
    public @NotNull Action<Void> removeUser(@NotNull User user) {
        Route.Compiled route = Route.Ticket.REMOVE_USER.compile(this.id, user.getId());

        return new ActionImpl<>(this.getAPI(), route) {
            @Override
            protected @Nullable JsonElement getRequestBody() {
                return null;
            }
        };
    }
}
