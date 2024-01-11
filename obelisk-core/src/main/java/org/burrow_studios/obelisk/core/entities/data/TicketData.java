package org.burrow_studios.obelisk.core.entities.data;

import com.google.gson.*;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.*;
import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public final class TicketData extends Data<TicketImpl> {
    public TicketData() {
        super();
    }

    public TicketData(long id) {
        super(id);
    }

    public TicketData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull TicketImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

        final long   id       = json.get("id").getAsLong();
        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final Ticket.State state = Ticket.State.valueOf(stateStr);

        final ArrayList<String> tags = buildList(json, "tags", JsonElement::getAsString);

        final DelegatingTurtleCacheView<UserImpl> users = buildDelegatingCacheView(json, "users", api.getUsers(), UserImpl.class);

        final TicketImpl ticket = new TicketImpl(api, id, title, state, tags, users);

        api.getTickets().add(ticket);
        return ticket;
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

    public void setTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        this.set("tags", arr);
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
}
