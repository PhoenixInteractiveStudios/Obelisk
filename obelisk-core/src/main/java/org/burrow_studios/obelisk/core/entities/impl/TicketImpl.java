package org.burrow_studios.obelisk.core.entities.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.action.DeleteActionImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.action.ticket.TicketModifierImpl;
import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;
import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildList;

public final class TicketImpl extends TurtleImpl implements Ticket {
    private @Nullable String title;
    private @NotNull State state;
    private final @NotNull List<String> tags;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> users;

    public TicketImpl(
            @NotNull ObeliskImpl api,
            long id,
            @Nullable String title,
            @NotNull State state,
            @NotNull List<String> tags,
            @NotNull DelegatingTurtleCacheView<UserImpl> users
    ) {
        super(api, id);
        this.title = title;
        this.state = state;
        this.tags = tags;
        this.users = users;
    }

    public TicketImpl(@NotNull ObeliskImpl api, @NotNull EntityData data) {
        super(api, data.getId());

        final JsonObject json = data.toJson();

        this.title = json.get("title").getAsString();

        final String stateStr = json.get("state").getAsString();
        this.state = Ticket.State.valueOf(stateStr);

        this.tags = buildList(json, "tags", JsonElement::getAsString);
        this.users = buildDelegatingCacheView(json, "users", api.getUsers(), UserImpl.class);

        api.getTickets().add(this);
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("title", title);
        json.addProperty("state", state.name());

        JsonArray tagJson = new JsonArray();
        for (String tag : this.tags)
            tagJson.add(tag);
        json.add("tags", tagJson);

        JsonArray userIds = new JsonArray();
        for (long userId : this.users.getIdsAsImmutaleSet())
            userIds.add(userId);
        json.add("users", userIds);

        return json;
    }

    @Override
    public @NotNull TicketModifierImpl modify() {
        return new TicketModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Ticket> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                Ticket.class,
                this.getId(),
                Endpoints.Ticket.DELETE.builder()
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @Nullable String getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    public void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public @NotNull List<String> getTags() {
        return List.copyOf(this.tags);
    }

    public @NotNull List<String> getTagsMutable() {
        return this.tags;
    }

    @Override
    public @NotNull DelegatingTurtleCacheView<UserImpl> getUsers() {
        return this.users;
    }

    @Override
    public @NotNull ActionImpl<Ticket> addUser(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Ticket.ADD_USER.builder()
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }

    @Override
    public @NotNull ActionImpl<Ticket> removeUser(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Ticket.DEL_USER.builder()
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }
}
