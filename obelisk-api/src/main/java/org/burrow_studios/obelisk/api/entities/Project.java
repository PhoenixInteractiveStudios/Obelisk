package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.Action;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.cache.EntitySet;
import org.jetbrains.annotations.NotNull;

public interface Project extends IEntity {
    String IDENTIFIER = "project";

    @Override
    @NotNull ProjectModifier modify();

    @Override
    @NotNull DeleteAction<Project> delete();

    /* - - - - - - - - - - */

    @NotNull String getTitle();

    @NotNull State getState();

    @NotNull EntitySet<? extends User> getMembers();

    @NotNull Action<Void> addUser(@NotNull User user);

    @NotNull Action<Void> removeUser(@NotNull User user);

    /* - - - - - - - - - - */

    enum State {
        CONCEPT,
        PLANNING,
        APPLICATION,
        RUNNING,
        ENDED,
        STOPPED,
        CANCELED
    }
}
