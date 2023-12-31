package org.burrow_studios.obelisk.internal.action.entity.group;

import org.burrow_studios.obelisk.api.action.entity.group.GroupBuilder;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.BuilderImpl;
import org.burrow_studios.obelisk.internal.data.GroupData;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class GroupBuilderImpl extends BuilderImpl<Group, GroupData> implements GroupBuilder {
    public GroupBuilderImpl(@NotNull ObeliskImpl api) {
        super(
                api,
                Group.class,
                Route.Group.CREATE.builder().compile(),
                new GroupData(),
                EntityBuilder::buildGroup
        );
    }

    @Override
    public @NotNull GroupBuilderImpl setName(@NotNull String name) {
        this.data.setName(name);
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl setPosition(int position) {
        this.data.setPosition(position);
        return this;
    }

    @Override
    public @NotNull GroupBuilderImpl addMembers(@NotNull User... users) {
        this.data.addMembers(users);
        return this;
    }
}
