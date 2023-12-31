package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TicketData extends Data<Ticket> {
    public TicketData() {
        super();
    }

    public TicketData(long id) {
        super(id);
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
