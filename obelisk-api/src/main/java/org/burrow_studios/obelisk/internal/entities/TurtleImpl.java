package org.burrow_studios.obelisk.internal.entities;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

public abstract class TurtleImpl implements Turtle {
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
}
