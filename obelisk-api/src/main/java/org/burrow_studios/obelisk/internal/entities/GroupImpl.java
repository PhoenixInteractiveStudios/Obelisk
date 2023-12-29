package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GroupImpl extends TurtleImpl implements Group {
    private @NotNull String name;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> members;
    private int position;

    public GroupImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String name,
            @NotNull DelegatingTurtleCacheView<UserImpl> members,
            int position
    ) {
        super(api, id);
        this.name = name;
        this.members = members;
        this.position = position;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public int getSize() {
        return this.members.size();
    }

    @Override
    public @NotNull TurtleSetView<UserImpl> getMembers() {
        return this.members;
    }

    @Override
    public @NotNull Set<Long> getMemberIds() {
        return this.members.getIdsAsImmutaleSet();
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
