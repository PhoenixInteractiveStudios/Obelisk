package org.burrow_studios.obelisk.monolith.entities;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.burrow_studios.obelisk.core.entities.AbstractProject;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectModifier;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectUserAddAction;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectUserRemoveAction;
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
    public @NotNull DatabaseProjectModifier modify() {
        return new DatabaseProjectModifier(this);
    }

    @Override
    public @NotNull DatabaseProjectDeleteAction delete() {
        return new DatabaseProjectDeleteAction(this);
    }

    @Override
    public @NotNull DatabaseProjectUserAddAction addUser(@NotNull User user) {
        return new DatabaseProjectUserAddAction(this, user);
    }

    @Override
    public @NotNull DatabaseProjectUserRemoveAction removeUser(@NotNull User user) {
        return new DatabaseProjectUserRemoveAction(this, user);
    }
}
