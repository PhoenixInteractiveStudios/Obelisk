package org.burrow_studios.obelisk.core.entities.action.group;

import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityUpdater;
import org.burrow_studios.obelisk.core.entities.checks.GroupChecks;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupModifierImpl extends ModifierImpl<Group, GroupImpl> implements GroupModifier {
    public GroupModifierImpl(@NotNull GroupImpl group) {
        super(
                group,
                Route.Group.EDIT.builder()
                        .withArg(group.getId())
                        .compile(),
                EntityUpdater::updateGroup
        );
    }

    @Override
    public @NotNull GroupModifierImpl setName(@NotNull String name) throws IllegalArgumentException {
        GroupChecks.checkName(name);
        data.set("name", new JsonPrimitive(name));
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl setPosition(int position) throws IllegalArgumentException {
        GroupChecks.checkPosition(position);
        data.set("position", new JsonPrimitive(position));
        return this;
    }
}
