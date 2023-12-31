package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.api.action.entity.board.BoardBuilder;
import org.burrow_studios.obelisk.api.action.entity.group.GroupBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Obelisk {
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

    @NotNull GroupBuilder createGroup();

    @NotNull ProjectBuilder createProject();

    @NotNull TicketBuilder createTicket();

    @NotNull UserBuilder createUser();

    @NotNull BoardBuilder createBoard();
}
