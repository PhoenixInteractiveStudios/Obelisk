package org.burrow_studios.obelisk.internal.entities;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.entities.board.BoardImpl;
import org.burrow_studios.obelisk.internal.entities.board.IssueImpl;
import org.burrow_studios.obelisk.internal.entities.board.TagImpl;
import org.jetbrains.annotations.NotNull;

public abstract sealed class TurtleImpl<T extends Turtle> implements Turtle permits GroupImpl, ProjectImpl, TicketImpl, UserImpl, BoardImpl, IssueImpl, TagImpl {
    protected final @NotNull ObeliskImpl api;
    protected final long id;

    protected TurtleImpl(@NotNull ObeliskImpl api, long id) {
        this.api = api;
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }

    @Override
    public final @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Turtle turtle)) return false;
        return turtle.getId() == this.getId();
    }

    public @NotNull JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        return json;
    }
}
