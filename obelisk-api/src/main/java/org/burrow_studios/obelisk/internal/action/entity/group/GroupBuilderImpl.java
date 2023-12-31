package org.burrow_studios.obelisk.internal.action.entity.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.group.GroupBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupBuilderImpl extends BuilderImpl<Group> implements GroupBuilder {
    public GroupBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Group.class,
                Route.Group.CREATE.builder().compile(),
                EntityBuilder::buildGroup
        );
    }

    @Override
    public @NotNull GroupBuilderImpl setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl setPosition(int position) {
        this.set("position", new JsonPrimitive(position));
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.add("members", arr);
        return this;
    }
}
