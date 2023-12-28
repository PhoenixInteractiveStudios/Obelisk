package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GroupImpl extends TurtleImpl implements Group {
    private final @NotNull String name;
    private final int size;
    private final @NotNull Set<Long> memberIds;
    private final int position;

    public GroupImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String name,
            int size,
            @NotNull Set<Long> memberIds,
            int position
    ) {
        super(api, id);
        this.name = name;
        this.size = size;
        this.memberIds = memberIds;
        this.position = position;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public @NotNull Set<UserImpl> getMembers() {
        // TODO
        return Set.of();
    }

    @Override
    public @NotNull Set<Long> getMemberIds() {
        return Set.copyOf(this.memberIds);
    }

    @Override
    public int getPosition() {
        return this.position;
    }
}
