package org.burrow_studios.obelisk.core.entities.action.group;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.handleUpdate;
import static org.burrow_studios.obelisk.core.entities.UpdateHelper.handleUpdateTurtles;

public class GroupModifierImpl extends ModifierImpl<Group, GroupImpl> implements GroupModifier {
    public GroupModifierImpl(@NotNull GroupImpl group) {
        super(
                group,
                Route.Group.EDIT.builder()
                        .withArg(group.getId())
                        .compile(),
                GroupModifierImpl::update
        );
    }

    protected static void update(@NotNull EntityData data, @NotNull GroupImpl group) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "name", JsonElement::getAsString, group::setName);
        handleUpdate(json, "position", JsonElement::getAsInt, group::setPosition);
        handleUpdateTurtles(json, "members", group::getMembers);
    }

    @Override
    public @NotNull GroupModifierImpl setName(@NotNull String name) {
        data.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl setPosition(int position) {
        data.set("position", new JsonPrimitive(position));
        return this;
    }
}
