package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.api.action.entity.board.BoardBuilder;
import org.burrow_studios.obelisk.api.action.entity.group.GroupBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.cache.TurtleSetView;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.event.EventHandler;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Obelisk {
    @NotNull EventHandler getEventHandler();

    <T extends Turtle> @Nullable T getTurtle(long id, @NotNull Class<T> type);

    @Nullable Turtle getTurtle(long id);

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

    @NotNull TurtleSetView<? extends Issue> getIssues();

    @Nullable Issue getIssue(long id);

    @NotNull TurtleSetView<? extends Tag> getTags();

    @Nullable Tag getTag(long id);

    @NotNull GroupBuilder createGroup();

    @NotNull ProjectBuilder createProject();

    @NotNull TicketBuilder createTicket();

    @NotNull UserBuilder createUser();

    @NotNull BoardBuilder createBoard();
}
