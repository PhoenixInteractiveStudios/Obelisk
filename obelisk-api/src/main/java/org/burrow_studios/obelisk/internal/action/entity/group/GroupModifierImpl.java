package org.burrow_studios.obelisk.internal.action.entity.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityUpdater;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupModifierImpl extends ModifierImpl<Group> implements GroupModifier {
    public GroupModifierImpl(@NotNull GroupImpl group) {
        super(
                group,
                Route.Group.EDIT.builder()
                        .withArg(group.getId())
                        .compile(),
                json -> EntityUpdater.updateGroup(group, json)
        );
    }

    @Override
    public @NotNull GroupModifierImpl setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl setPosition(int position) {
        this.set("position", new JsonPrimitive(position));
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl addMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.add("members", arr);
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl removeMembers(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.remove("members", arr);
        return this;
    }
}
