package org.burrow_studios.obelisk.core.entities.action.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityUpdater;
import org.burrow_studios.obelisk.core.entities.checks.TicketChecks;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketModifierImpl extends ModifierImpl<Ticket, TicketImpl> implements TicketModifier {
    public TicketModifierImpl(@NotNull TicketImpl ticket) {
        super(
                ticket,
                Endpoints.Ticket.EDIT.builder()
                        .withArg(ticket.getId())
                        .compile(),
                EntityUpdater::updateTicket
        );
    }

    @Override
    public @NotNull TicketModifierImpl setTitle(@Nullable String title) throws IllegalArgumentException {
        TicketChecks.checkTitle(title);
        data.set("title", (title == null)
                ? JsonNull.INSTANCE
                : new JsonPrimitive(title)
        );
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl setState(@NotNull Ticket.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl setTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        data.set("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl addTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        data.addToArray("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl removeTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        data.removeFromArray("tags", arr);
        return this;
    }
}
