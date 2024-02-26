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
import org.burrow_studios.obelisk.api.event.EventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is the core of the API. It functions as the base interface to all features and is responsible for caching and
 * delegating tasks.
 * <br> Instances can be created by using the {@link ObeliskBuilder}.
 * @see Turtle
 */
public interface Obelisk {
    /**
     * Returns the EventHandler of this instance. The EventHandler can be used to register {@link EventListener
     * EventListeners}:
     * <pre> {@code
     * Obelisk api = ...;
     * EventListener listener = new EventListener() {
     *     @Override
     *     public void onGenericEvent(@NotNull Event event) {
     *         // ...
     *     }
     * };
     * client.getEventHandler().registerListener(listener);
     * } </pre>
     * @return EventHandler instance.
     */
    @NotNull EventHandler getEventHandler();

    /**
     * Returns a {@link Turtle} of type {@code T} specified by its id, or {@code null} if no such object is stored in
     * the underlying cache, or if it is of a different type.
     * @param id The unique id of the Turtle.
     * @param type Subclass of {@link Turtle}.
     * @param <T> Subclass of {@link Turtle}.
     * @return The requested Turtle (may be {@code null}).
     * @see Turtle#getId()
     */
    <T extends Turtle> @Nullable T getTurtle(long id, @NotNull Class<T> type);

    /**
     * Returns a {@link Turtle} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the Turtle.
     * @return The requested Turtle (may be {@code null}).
     * @see Turtle#getId()
     */
    @Nullable Turtle getTurtle(long id);

    /**
     * Returns an immutable List of all cached {@link Group} objects.
     * @return List of cached Groups.
     */
    @NotNull TurtleSetView<? extends Group> getGroups();

    /**
     * Returns a {@link Group} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link Group}.
     * @return The requested Group (may be {@code null}).
     * @see Group#getId()
     */
    @Nullable Group getGroup(long id);

    /**
     * Returns an immutable List of all cached {@link Project} objects.
     * @return List of cached Projects.
     */
    @NotNull TurtleSetView<? extends Project> getProjects();

    /**
     * Returns a {@link Project} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link Project}.
     * @return The requested Project (may be {@code null}).
     * @see Project#getId()
     */
    @Nullable Project getProject(long id);

    /**
     * Returns an immutable List of all cached {@link Ticket} objects.
     * @return List of cached Tickets.
     */
    @NotNull TurtleSetView<? extends Ticket> getTickets();

    /**
     * Returns a {@link Ticket} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link Ticket}.
     * @return The requested Ticket (may be {@code null}).
     * @see Ticket#getId()
     */
    @Nullable Ticket getTicket(long id);

    /**
     * Returns an immutable List of all cached {@link User} objects.
     * @return List of cached Users.
     */
    @NotNull TurtleSetView<? extends User> getUsers();

    /**
     * Returns a {@link User} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link User}.
     * @return The requested User (may be {@code null}).
     * @see User#getId()
     */
    @Nullable User getUser(long id);

    /**
     * Returns an immutable List of all cached {@link Board} objects.
     * @return List of cached Boards.
     */
    @NotNull TurtleSetView<? extends Board> getBoards();

    /**
     * Returns a {@link Board} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link Board}.
     * @return The requested Board (may be {@code null}).
     * @see Board#getId()
     */
    @Nullable Board getBoard(long id);

    /**
     * Returns an immutable List of all cached {@link Issue} objects.
     * @return List of cached Issues.
     */
    @NotNull TurtleSetView<? extends Issue> getIssues();

    /**
     * Returns a {@link Issue} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link Issue}.
     * @return The requested Issue (may be {@code null}).
     * @see Issue#getId()
     */
    @Nullable Issue getIssue(long id);

    /**
     * Returns an immutable List of all cached {@link Tag} objects.
     * @return List of cached Tags.
     */
    @NotNull TurtleSetView<? extends Tag> getTags();

    /**
     * Returns a {@link Tag} specified by its id, or {@code null} if no such object is stored in the underlying cache.
     * @param id The unique id of the {@link Tag}.
     * @return The requested Tag (may be {@code null}).
     * @see Tag#getId()
     */
    @Nullable Tag getTag(long id);

    /**
     * Creates a builder for request to create a new {@link Group}. The returned builder may be used to modify the
     * request and to set each required field. If any required field is missing the server will reject the request and
     * respond with an error.
     * <br> If the operation is successful, the created Group will also be put into the cache.
     * @return Action that provides the newly created {@link Group} on completion.
     */
    @NotNull GroupBuilder createGroup();

    /**
     * Creates a builder for request to create a new {@link Project}. The returned builder may be used to modify the
     * request and to set each required field. If any required field is missing the server will reject the request and
     * respond with an error.
     * <br> If the operation is successful, the created Project will also be put into the cache.
     * @return Action that provides the newly created {@link Project} on completion.
     */
    @NotNull ProjectBuilder createProject();

    /**
     * Creates a builder for request to create a new {@link Ticket}. The returned builder may be used to modify the
     * request and to set each required field. If any required field is missing the server will reject the request and
     * respond with an error.
     * <br> If the operation is successful, the created Ticket will also be put into the cache.
     * @return Action that provides the newly created {@link Ticket} on completion.
     */
    @NotNull TicketBuilder createTicket();

    /**
     * Creates a builder for request to create a new {@link User}. The returned builder may be used to modify the
     * request and to set each required field. If any required field is missing the server will reject the request and
     * respond with an error.
     * <br> If the operation is successful, the created User will also be put into the cache.
     * @return Action that provides the newly created {@link User} on completion.
     */
    @NotNull UserBuilder createUser();

    /**
     * Creates a builder for request to create a new {@link Board}. The returned builder may be used to modify the
     * request and to set each required field. If any required field is missing the server will reject the request and
     * respond with an error.
     * <br> If the operation is successful, the created Board will also be put into the cache.
     * @return Action that provides the newly created {@link Board} on completion.
     */
    @NotNull BoardBuilder createBoard();
}
