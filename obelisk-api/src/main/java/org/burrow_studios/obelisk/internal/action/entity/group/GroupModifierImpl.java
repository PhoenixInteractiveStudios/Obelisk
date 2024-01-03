package org.burrow_studios.obelisk.internal.action.entity.group;

import org.burrow_studios.obelisk.api.action.entity.group.GroupModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.data.GroupData;
import org.burrow_studios.obelisk.internal.entities.GroupImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupModifierImpl extends ModifierImpl<Group, GroupImpl, GroupData> implements GroupModifier {
    public GroupModifierImpl(@NotNull GroupImpl group) {
        super(
                group,
                Route.Group.EDIT.builder()
                        .withArg(group.getId())
                        .compile(),
                new GroupData(group.getId()),
                GroupData::new
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
}
