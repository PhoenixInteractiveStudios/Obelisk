package org.burrow_studios.obelisk.internal.data;

import com.google.gson.*;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.entities.TicketImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TicketData extends Data<Ticket, TicketImpl> {
    public TicketData() {
        super();
    }

    public TicketData(long id) {
        super(id);
    }

    @Override
    public @NotNull TicketImpl build(@NotNull EntityBuilder builder) {
        return builder.buildTicket(toJson());
    }

    @Override
    public void update(@NotNull TicketImpl ticket) {
        final JsonObject json = toJson();

        handleUpdate(json, "title", JsonElement::getAsString, ticket::setTitle);
        handleUpdateEnum(json, "state", Ticket.State.class, ticket::setState);
        handleUpdateArray(json, "tags", JsonElement::getAsString, ticket.getTagsMutable());
        handleUpdateTurtles(json, "users", ticket::getUsers);
    }

    public void setTitle(@Nullable String title) {
        this.set("title", (title == null)
                ? JsonNull.INSTANCE
                : new JsonPrimitive(title)
        );
    }

    public void setState(@NotNull Ticket.State state) {
        this.set("state", new JsonPrimitive(state.name()));
    }

    public void addTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        this.addToArray("tags", arr);
    }

    public void removeTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        this.removeFromArray("tags", arr);
    }

    public void addUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.addToArray("users", arr);
    }

    public void removeUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.removeFromArray("users", arr);
    }
}
