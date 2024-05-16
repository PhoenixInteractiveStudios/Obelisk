package org.burrow_studios.obelisk.client.action.entity.ticket;

import com.google.gson.JsonNull;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.client.action.ModifierImpl;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketModifierImpl extends ModifierImpl<Ticket> implements TicketModifier {
    public TicketModifierImpl(@NotNull Ticket entity) {
        super(entity, createRoute(entity));
    }

    private static Route.Compiled createRoute(@NotNull Ticket entity) {
        return Route.Ticket.EDIT_TICKET.compile(entity.getId());
    }

    @Override
    public @NotNull TicketModifierImpl setTitle(@Nullable String title) {
        if (title == null)
            this.data.add("title", JsonNull.INSTANCE);
        else
            this.data.addProperty("title", title);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl setState(@NotNull Ticket.State state) {
        this.data.addProperty("state", state.name());
        return this;
    }
}
