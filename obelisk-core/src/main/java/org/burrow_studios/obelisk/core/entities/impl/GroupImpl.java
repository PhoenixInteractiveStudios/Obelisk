package org.burrow_studios.obelisk.core.entities.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.action.DeleteActionImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.action.group.GroupModifierImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;

public final class GroupImpl extends TurtleImpl implements Group {
    private @NotNull String name;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> members;
    private int position;

    public GroupImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String name,
            @NotNull DelegatingTurtleCacheView<UserImpl> members,
            int position
    ) {
        super(api, id);
        this.name = name;
        this.members = members;
        this.position = position;
    }

    public GroupImpl(@NotNull ObeliskImpl api, @NotNull EntityData data) {
        super(api, data.getId());

        final JsonObject json = data.toJson();

        this.name     = json.get("name").getAsString();
        this.position = json.get("position").getAsInt();

        this.members = buildDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

        api.getGroups().add(this);
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("name", name);

        JsonArray memberIds = new JsonArray();
        for (long memberId : this.members.getIdsAsImmutaleSet())
            memberIds.add(memberId);
        json.add("members", memberIds);

        json.addProperty("position", position);
        return json;
    }

    @Override
    public @NotNull GroupModifierImpl modify() {
        return new GroupModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Group> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                Group.class,
                this.getId(),
                Endpoints.Group.DELETE.builder()
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public int getSize() {
        return this.members.size();
    }

    @Override
    public @NotNull DelegatingTurtleCacheView<UserImpl> getMembers() {
        return this.members;
    }

    @Override
    public @NotNull ActionImpl<Group> addMember(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Group.ADD_MEMBER.builder()
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }

    @Override
    public @NotNull ActionImpl<Group> removeMember(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Endpoints.Group.DEL_MEMBER.builder()
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
