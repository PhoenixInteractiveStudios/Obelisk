package org.burrow_studios.obelisk.server.db;

import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.db.entity.BoardDB;
import org.burrow_studios.obelisk.server.db.entity.ProjectDB;
import org.burrow_studios.obelisk.server.db.entity.TicketDB;
import org.burrow_studios.obelisk.server.db.entity.GroupDB;
import org.burrow_studios.obelisk.server.db.entity.UserDB;
import org.jetbrains.annotations.NotNull;

public class EntityProvider implements DataProvider {
    private final ObeliskServer server;
    private final EntityDatabase database;

    public EntityProvider(@NotNull ObeliskServer server) {
        // TODO
        final String host     = "null";
        final int    port     = 3306;
        final String database = "null";
        final String user     = "null";
        final String pass     = "null";

        this.server = server;
        this.database = new EntityDatabase(host, port, database, user, pass);
    }

    public @NotNull GroupDB getGroupDB() {
        return this.database;
    }

    public @NotNull ProjectDB getProjectDB() {
        return this.database;
    }

    public @NotNull TicketDB getTicketDB() {
        return this.database;
    }

    public @NotNull UserDB getUserDB() {
        return this.database;
    }

    public @NotNull BoardDB getBoardDB() {
        return this.database;
    }

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.server.getAPI();
    }

    @Override
    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        // TODO
        return null;
    }
}
