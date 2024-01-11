package org.burrow_studios.obelisk.core.entities.action.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.group.GroupBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.BuilderImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.checks.GroupChecks;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.buildDelegatingCacheView;

public class GroupBuilderImpl extends BuilderImpl<Group> implements GroupBuilder {
    public GroupBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Group.class,
                Route.Group.CREATE.builder().compile(),
                GroupBuilderImpl::build
        );
    }

    protected static @NotNull GroupImpl build(@NotNull EntityData data, @NotNull ObeliskImpl api) {
        final JsonObject json = data.toJson();

        final long   id       = json.get("id").getAsLong();
        final String name     = json.get("name").getAsString();
        final int    position = json.get("position").getAsInt();

        final DelegatingTurtleCacheView<UserImpl> members = buildDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

        GroupChecks.checkName(name);
        GroupChecks.checkPosition(position);

        final GroupImpl group = new GroupImpl(api, id, name, members, position);

        api.getGroups().add(group);
        return group;
    }

    @Override
    public @NotNull GroupBuilderImpl setName(@NotNull String name) {
        data.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl setPosition(int position) {
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
