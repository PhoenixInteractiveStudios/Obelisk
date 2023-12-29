package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Obelisk {
    /* - GENERAL - */

    @NotNull TurtleSetView<? extends Group> getGroups();

    @Nullable Group getGroup(long id);

    @NotNull TurtleSetView<? extends Project> getProjects();

    @Nullable Project getProject(long id);

    @NotNull TurtleSetView<? extends Ticket> getTickets();

    @Nullable Ticket getTicket(long id);

    @NotNull TurtleSetView<? extends User> getUsers();

    @Nullable User getUser(long id);

    @NotNull TurtleSetView<? extends Board> getBoards();

    @Nullable Board getBoard(long id);
}
