package org.burrow_studios.obelisk.core.entities.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.BuildHelper.*;
import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public final class GroupData extends Data<GroupImpl> {
    public GroupData() {
        super();
    }

    public GroupData(long id) {
        super(id);
    }

    public GroupData(@NotNull JsonObject json) {
        super(json);
    }

    @Override
    public @NotNull GroupImpl build(@NotNull ObeliskImpl api) {
        final JsonObject json = toJson();

        final long   id       = json.get("id").getAsLong();
        final String name     = json.get("name").getAsString();
        final int    position = json.get("position").getAsInt();

        final DelegatingTurtleCacheView<UserImpl> members = buildDelegatingCacheView(json, "members", api.getUsers(), UserImpl.class);

        final GroupImpl group = new GroupImpl(api, id, name, members, position);

        api.getGroups().add(group);
        return group;
    }

    @Override
    public void update(@NotNull GroupImpl group) {
        final JsonObject json = toJson();

        handleUpdate(json, "name", JsonElement::getAsString, group::setName);
        handleUpdate(json, "position", JsonElement::getAsInt, group::setPosition);
        handleUpdateTurtles(json, "members", group::getMembers);
    }

    public void setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
    }

    public void setPosition(int position) {
        this.set("position", new JsonPrimitive(position));
    }

    public void addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.addToArray("members", arr);
    }
}
