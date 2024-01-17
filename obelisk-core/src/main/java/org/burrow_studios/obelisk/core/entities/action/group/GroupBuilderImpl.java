package org.burrow_studios.obelisk.core.entities.action.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.group.GroupBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.entities.checks.GroupChecks;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupBuilderImpl extends BuilderImpl<Group> implements GroupBuilder {
    public GroupBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Group.class,
                Route.Group.CREATE.builder().compile(),
                GroupImpl::new
        );
    }

    @Override
    public @NotNull GroupBuilderImpl setName(@NotNull String name) throws IllegalArgumentException {
        GroupChecks.checkName(name);
        data.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl setPosition(int position) throws IllegalArgumentException {
        GroupChecks.checkPosition(position);
        data.set("position", new JsonPrimitive(position));
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        data.addToArray("members", arr);
        return this;
    }
}
