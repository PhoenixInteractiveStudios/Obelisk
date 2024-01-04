package org.burrow_studios.obelisk.server.db;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.db.dedicated.board.BoardDB;
import org.burrow_studios.obelisk.server.db.dedicated.group.GroupDB;
import org.burrow_studios.obelisk.server.db.dedicated.project.ProjectDB;
import org.burrow_studios.obelisk.server.db.dedicated.ticket.TicketDB;
import org.burrow_studios.obelisk.server.db.dedicated.user.UserDB;
import org.jetbrains.annotations.NotNull;

public class DataProvider {
    private final ObeliskServer server;

    private final   BoardDB   boardDB;
    private final   GroupDB   groupDB;
    private final ProjectDB projectDB;
    private final  TicketDB  ticketDB;
    private final    UserDB    userDB;

    public DataProvider(@NotNull ObeliskServer server) {
        this.server = server;

        this.boardDB   = BoardDB.get();
        this.groupDB   = GroupDB.get();
        this.projectDB = ProjectDB.get();
        this.ticketDB  = TicketDB.get();
        this.userDB    = UserDB.get();
    }

    public ObeliskServer getServer() {
        return server;
    }

    public @NotNull BoardDB getBoardDB() {
        return boardDB;
    }

    public @NotNull GroupDB getGroupDB() {
        return groupDB;
    }

    public @NotNull ProjectDB getProjectDB() {
        return projectDB;
    }

    public @NotNull TicketDB getTicketDB() {
        return ticketDB;
    }

    public @NotNull UserDB getUserDB() {
        return userDB;
    }
}
