package org.burrow_studios.obelisk.internal.entities.issue;

import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.jetbrains.annotations.NotNull;

public class TagImpl extends TurtleImpl implements Tag {
    private final long boardId;
    private @NotNull String name;

    public TagImpl(
            @NotNull ObeliskImpl api,
            long id,
            long boardId,
            @NotNull String name
    ) {
        super(api, id);
        this.boardId = boardId;
        this.name = name;
    }

    @Override
    public long getBoardId() {
        return this.boardId;
    }

    @Override
    public @NotNull Board getBoard() {
        // TODO
        return null;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
