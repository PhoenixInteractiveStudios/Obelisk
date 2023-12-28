package org.burrow_studios.obelisk.internal;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.entities.*;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.internal.cache.TurtleCache;
import org.burrow_studios.obelisk.internal.entities.TurtleImpl;
import org.burrow_studios.obelisk.internal.event.EventHandlerImpl;
import org.burrow_studios.obelisk.internal.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

public class ObeliskImpl implements Obelisk {
    private final EventHandlerImpl eventHandler;
    private final NetworkHandler   networkHandler;

    private final TurtleCache<TurtleImpl> entityCache;

    public ObeliskImpl() {
        this.eventHandler   = new EventHandlerImpl(this);
        this.networkHandler = new NetworkHandler(this);

        this.entityCache = new TurtleCache<>();
    }

    public @NotNull EventHandlerImpl getEventHandler() {
        return eventHandler;
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    /* - ENTITIES - */

    @Override
    public @NotNull Set<? extends Turtle> getEntities() {
        return Set.copyOf(this.entityCache);
    }

    @Override
    public @Nullable Turtle getEntity(long id) {
        return this.entityCache.get(id);
    }

    @Override
    public @NotNull Set<? extends Group> getGroups() {
        return this.entityCache.stream()
                .filter(Group.class::isInstance)
                .map(Group.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable Group getGroup(long id) {
        return this.entityCache.get(id, Group.class);
    }

    @Override
    public @NotNull Set<? extends Project> getProjects() {
        return this.entityCache.stream()
                .filter(Project.class::isInstance)
                .map(Project.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable Project getProject(long id) {
        return this.entityCache.get(id, Project.class);
    }

    @Override
    public @NotNull Set<? extends Ticket> getTickets() {
        return this.entityCache.stream()
                .filter(Ticket.class::isInstance)
                .map(Ticket.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable Ticket getTicket(long id) {
        return this.entityCache.get(id, Ticket.class);
    }

    @Override
    public @NotNull Set<? extends User> getUsers() {
        return this.entityCache.stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable User getUser(long id) {
        return this.entityCache.get(id, User.class);
    }

    @Override
    public @NotNull Set<? extends Board> getBoards() {
        return this.entityCache.stream()
                .filter(Board.class::isInstance)
                .map(Board.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable Board getBoard(long id) {
        return this.entityCache.get(id, Board.class);
    }
}
