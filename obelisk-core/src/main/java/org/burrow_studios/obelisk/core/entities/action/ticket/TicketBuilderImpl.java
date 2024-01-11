package org.burrow_studios.obelisk.core.entities.action.ticket;

import com.google.gson.*;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.TicketImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;
import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildList;

public class TicketBuilderImpl extends BuilderImpl<Ticket> implements TicketBuilder {
    public TicketBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Ticket.class,
                Route.Ticket.CREATE.builder().compile(),
                TicketBuilderImpl::build
        );
    }

    protected static @NotNull TicketImpl build(@NotNull EntityData data, @NotNull ObeliskImpl api) {
        final JsonObject json = data.toJson();

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
    public @NotNull TicketBuilderImpl setTitle(@Nullable String title) {
        data.set("title", (title == null)
                ? JsonNull.INSTANCE
                : new JsonPrimitive(title)
        );
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl setState(@NotNull Ticket.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addTags(@NotNull String... tags) {
        JsonArray arr = new JsonArray();
        for (String tag : tags)
            arr.add(tag);
        data.addToArray("tags", arr);
        return this;
    }

    @Override
    public @NotNull TicketBuilderImpl addUsers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        data.addToArray("users", arr);
        return this;
    }
}
