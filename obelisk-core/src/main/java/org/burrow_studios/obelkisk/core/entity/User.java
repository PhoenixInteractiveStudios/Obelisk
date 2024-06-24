package org.burrow_studios.obelkisk.core.entity;

import org.burrow_studios.obelkisk.core.db.interfaces.UserDB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class User {
    private final long id;
    private final UserDB database;

    public User(long id, @NotNull UserDB database) {
        this.id = id;
        this.database = database;
    }

    public long getId() {
        return this.id;
    }

    public @NotNull String getName() {
        return this.database.getUserName(this.id);
    }

    public @Nullable String getPronouns() {
        return this.database.getUserPronouns(this.id);
    }

    public void setName(@NotNull String name) {
        this.database.setUserName(this.id, name);
    }

    public void setPronouns(@NotNull String pronouns) {
        this.database.setUserPronouns(this.id, pronouns);
    }

    public void delete() {
        this.database.deleteUser(this.id);
    }
}
