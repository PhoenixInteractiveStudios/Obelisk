package org.burrow_studios.obelisk.server.users;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.users.db.group.GroupDB;
import org.burrow_studios.obelisk.server.users.db.user.UserDB;
import org.jetbrains.annotations.NotNull;

public class UserService {
    private final ObeliskServer server;

    private final GroupDB groupDB;
    private final  UserDB  userDB;

    public UserService(@NotNull ObeliskServer server) {
        this.server = server;

        this.groupDB = GroupDB.get();
        this.userDB  = UserDB.get();
    }

    public @NotNull GroupDB getGroupDB() {
        return this.groupDB;
    }

    public @NotNull UserDB getUserDB() {
        return this.userDB;
    }
}
