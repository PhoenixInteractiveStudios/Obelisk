package org.burrow_studios.obelisk.server.moderation;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.moderation.db.project.ProjectDB;
import org.burrow_studios.obelisk.server.moderation.db.ticket.TicketDB;
import org.jetbrains.annotations.NotNull;

public class ModerationService {
    private final ObeliskServer server;

    private final ProjectDB projectDB;
    private final  TicketDB  ticketDB;

    public ModerationService(@NotNull ObeliskServer server) {
        this.server = server;

        this.projectDB = ProjectDB.get(this);
        this.ticketDB  = TicketDB.get(this);
    }

    public @NotNull ProjectDB getProjectDB() {
        return this.projectDB;
    }

    public @NotNull TicketDB getTicketDB() {
        return this.ticketDB;
    }
}
