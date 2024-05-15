package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractProject extends AbstractEntity implements Project {
    private @NotNull String title;
    private @NotNull State state;
    private final @NotNull OrderedEntitySetView<AbstractUser> members;

    protected AbstractProject(
            @NotNull AbstractObelisk obelisk,
            long id,
            @NotNull String title,
            @NotNull State state,
            @NotNull OrderedEntitySetView<AbstractUser> members
    ) {
        super(obelisk, id);
        this.title = title;
        this.state = state;
        this.members = members;
    }

    @Override
    public final @NotNull String getTitle() {
        return this.title;
    }

    public final void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public final @NotNull State getState() {
        return this.state;
    }

    public final void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public final @NotNull OrderedEntitySetView<AbstractUser> getMembers() {
        return this.members;
    }
}
