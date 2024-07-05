package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.core.db.interfaces.UserDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DatabaseUser implements User {
    private final long id;
    private final UserDB database;

    public DatabaseUser(long id, @NotNull UserDB database) {
        this.id = id;
        this.database = database;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public @NotNull String getName() {
        return this.database.getUserName(this.id);
    }

    @Override
    public @Nullable String getPronouns() {
        return this.database.getUserPronouns(this.id);
    }

    @Override
    public void setName(@NotNull String name) {
        this.database.setUserName(this.id, name);
    }

    @Override
    public void setPronouns(@NotNull String pronouns) {
        this.database.setUserPronouns(this.id, pronouns);
    }

    @Override
    public void delete() {
        this.database.deleteUser(this.id);
    }
}
