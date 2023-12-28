package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Obelisk {
    @NotNull Set<? extends Turtle> getEntities();

    @Nullable Turtle getEntity(long id);

    /* - GENERAL - */

    @NotNull Set<? extends Group> getGroups();

    @Nullable Group getGroup(long id);

    @NotNull Set<? extends Project> getProjects();

    @Nullable Project getProject(long id);

    @NotNull Set<? extends Ticket> getTickets();

    @Nullable Ticket getTicket(long id);

    @NotNull Set<? extends User> getUsers();

    @Nullable User getUser(long id);

    @NotNull Set<? extends Board> getBoards();

    @Nullable Board getBoard(long id);
}
