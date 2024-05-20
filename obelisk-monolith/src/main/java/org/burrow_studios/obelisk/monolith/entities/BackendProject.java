package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.jetbrains.annotations.NotNull;

public class BackendProject extends AbstractProject {
    public BackendProject(
            @NotNull ObeliskMonolith obelisk,
            long id,
            @NotNull String title,
            @NotNull State state,
            @NotNull OrderedEntitySetView<AbstractUser> members
    ) {
        super(obelisk, id, title, state, members);
    }

    @Override
    public @NotNull ProjectModifier modify() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DeleteAction<Project> delete() {
        // TODO
        return null;
    }

    @Override
    public @NotNull Action<Void> addUser(@NotNull User user) {
        // TODO
        return null;
    }

    @Override
    public @NotNull Action<Void> removeUser(@NotNull User user) {
        // TODO
        return null;
    }
}
