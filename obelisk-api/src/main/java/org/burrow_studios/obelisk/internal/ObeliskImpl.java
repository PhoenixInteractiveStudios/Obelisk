package org.burrow_studios.obelisk.internal;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.internal.cache.TurtleCache;
import org.burrow_studios.obelisk.internal.entities.*;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.burrow_studios.obelisk.internal.event.EventHandlerImpl;
import org.burrow_studios.obelisk.internal.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ObeliskImpl implements Obelisk {
    private final EventHandlerImpl eventHandler;
    private final NetworkHandler   networkHandler;

    private final TurtleCache<GroupImpl>     groupCache;
    private final TurtleCache<ProjectImpl> projectCache;
    private final TurtleCache<TicketImpl>   ticketCache;
    private final TurtleCache<UserImpl>       userCache;
    private final TurtleCache<BoardImpl>     boardCache;

    public ObeliskImpl() {
        this.eventHandler   = new EventHandlerImpl(this);
        this.networkHandler = new NetworkHandler(this);

        this.groupCache   = new TurtleCache<>();
        this.projectCache = new TurtleCache<>();
        this.ticketCache  = new TurtleCache<>();
        this.userCache    = new TurtleCache<>();
        this.boardCache   = new TurtleCache<>();
    }

    public @NotNull EventHandlerImpl getEventHandler() {
        return eventHandler;
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    /* - ENTITIES - */

    @Override
    public @NotNull Set<TurtleImpl> getEntities() {
        final TurtleCache<TurtleImpl> entities = new TurtleCache<>();
        entities.addAll(this.groupCache);
        entities.addAll(this.projectCache);
        entities.addAll(this.ticketCache);
        entities.addAll(this.userCache);
        entities.addAll(this.boardCache);
        // TODO: add issues & tags?
        return Set.copyOf(entities);
    }

    @Override
    public @Nullable TurtleImpl getEntity(long id) {
        TurtleImpl turtle;
        turtle = this.groupCache.get(id);
        if (turtle != null) return turtle;
        turtle = this.projectCache.get(id);
        if (turtle != null) return turtle;
        turtle = this.ticketCache.get(id);
        if (turtle != null) return turtle;
        turtle = this.userCache.get(id);
        if (turtle != null) return turtle;
        turtle = this.boardCache.get(id);
        return turtle;
    }

    @Override
    public @NotNull Set<GroupImpl> getGroups() {
        return Set.copyOf(this.groupCache);
    }

    @Override
    public @Nullable GroupImpl getGroup(long id) {
        return this.groupCache.get(id);
    }

    @Override
    public @NotNull Set<ProjectImpl> getProjects() {
        return Set.copyOf(this.projectCache);
    }

    @Override
    public @Nullable ProjectImpl getProject(long id) {
        return this.projectCache.get(id);
    }

    @Override
    public @NotNull Set<TicketImpl> getTickets() {
        return Set.copyOf(ticketCache);
    }

    @Override
    public @Nullable TicketImpl getTicket(long id) {
        return this.ticketCache.get(id);
    }

    @Override
    public @NotNull Set<UserImpl> getUsers() {
        return Set.copyOf(this.userCache);
    }

    @Override
    public @Nullable UserImpl getUser(long id) {
        return this.userCache.get(id);
    }

    @Override
    public @NotNull Set<BoardImpl> getBoards() {
        return Set.copyOf(this.boardCache);
    }

    @Override
    public @Nullable BoardImpl getBoard(long id) {
        return this.boardCache.get(id);
    }
}
