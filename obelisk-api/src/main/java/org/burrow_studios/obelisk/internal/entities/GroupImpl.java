package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GroupImpl extends TurtleImpl implements Group {
    private @NotNull String name;
    private final @NotNull Set<Long> memberIds;
    private int position;

    public GroupImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String name,
            @NotNull Set<Long> memberIds,
            int position
    ) {
        super(api, id);
        this.name = name;
        this.memberIds = memberIds;
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
        return this.memberIds.size();
    }

    @Override
    public @NotNull Set<UserImpl> getMembers() {
        // TODO
        return Set.of();
    }

    public @NotNull Set<UserImpl> getMembersMutable() {
        // TODO
        return Set.of();
    }

    @Override
    public @NotNull Set<Long> getMemberIds() {
        return Set.copyOf(this.memberIds);
    }

    public @NotNull Set<Long> getMemberIdsMutable() {
        return this.memberIds;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
