package org.burrow_studios.obelisk.internal.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.DeleteActionImpl;
import org.burrow_studios.obelisk.internal.action.entity.GroupModifierImpl;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

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
                Route.Group.DELETE.builder()
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
    public @NotNull TurtleSetView<UserImpl> getMembers() {
        return this.members;
    }

    @Override
    public @NotNull Set<Long> getMemberIds() {
        return this.members.getIdsAsImmutaleSet();
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
