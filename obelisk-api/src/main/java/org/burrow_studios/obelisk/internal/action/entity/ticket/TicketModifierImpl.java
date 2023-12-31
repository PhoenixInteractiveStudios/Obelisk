package org.burrow_studios.obelisk.internal.action.entity.ticket;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketModifier;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityUpdater;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TicketModifierImpl extends ModifierImpl<Ticket> implements TicketModifier {
    public TicketModifierImpl(@NotNull TicketImpl ticket) {
        super(
                ticket,
                Route.Ticket.EDIT.builder()
                        .withArg(ticket.getId())
                        .compile(),
                json -> EntityUpdater.updateTicket(ticket, json)
        );
    }

    @Override
    public @NotNull TicketModifierImpl setTitle(@Nullable String title) {
        this.set("title", (title == null)
                ? JsonNull.INSTANCE
                : new JsonPrimitive(title)
        );
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl setState(@NotNull Ticket.State state) {
        this.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl addTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        this.add("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl removeTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        this.remove("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl addUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.remove("users", arr);
        return this;
    }

    @Override
    public @NotNull TicketModifierImpl removeUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.remove("users", arr);
        return this;
    }
}
