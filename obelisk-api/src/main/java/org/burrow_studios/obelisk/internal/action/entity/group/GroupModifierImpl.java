package org.burrow_studios.obelisk.internal.action.entity.group;

import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityUpdater;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.data.GroupData;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupModifierImpl extends ModifierImpl<Group, GroupData> implements GroupModifier {
    public GroupModifierImpl(@NotNull GroupImpl group) {
        super(
                group,
                Route.Group.EDIT.builder()
                        .withArg(group.getId())
                        .compile(),
                new GroupData(group.getId()),
                json -> EntityUpdater.updateGroup(group, json)
        );
    }

    @Override
    public @NotNull GroupModifierImpl setName(@NotNull String name) {
        this.data.setName(name);
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl setPosition(int position) {
        this.data.setPosition(position);
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl addMembers(@NotNull User... users) {
        this.data.addMembers(users);
        return this;
    }

    @Override
    public @NotNull GroupModifierImpl removeMembers(@NotNull User... users) {
        this.data.removeMembers(users);
        return this;
    }
}
