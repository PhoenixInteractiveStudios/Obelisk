package org.burrow_studios.obelisk.internal.action.entity.ticket;

import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.data.TicketData;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketModifierImpl extends ModifierImpl<Ticket, TicketImpl, TicketData> implements TicketModifier {
    public TicketModifierImpl(@NotNull TicketImpl ticket) {
        super(
                ticket,
                Route.Ticket.EDIT.builder()
                        .withArg(ticket.getId())
                        .compile(),
                new TicketData(ticket.getId()),
                TicketData::new
        );
    }

    @Override
    public @NotNull TicketModifierImpl setTitle(@Nullable String title) {
        this.data.setTitle(title);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl setState(@NotNull Ticket.State state) {
        this.data.setState(state);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl addTags(@NotNull String... tags) {
        this.data.addTags(tags);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl removeTags(@NotNull String... tags) {
        this.data.removeTags(tags);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl addUsers(@NotNull User... users) {
        this.data.addUsers(users);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl removeUsers(@NotNull User... users) {
        this.data.removeUsers(users);
        return this;
    }
}
