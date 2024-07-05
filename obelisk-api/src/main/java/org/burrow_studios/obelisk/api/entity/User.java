package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.entity.dao.UserDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class User {
    private final long id;
    private final UserDAO dao;

    public User(long id, @NotNull UserDAO dao) {
        this.id = id;
        this.dao = dao;
    }

    public long getId() {
        return this.id;
    }

    public @NotNull String getName() {
        return this.dao.getUserName(this.id);
    }

    public @Nullable String getPronouns() {
        return this.dao.getUserPronouns(this.id);
    }

    public void setName(@NotNull String name) {
        this.dao.setUserName(this.id, name);
    }

    public void setPronouns(@NotNull String pronouns) {
        this.dao.setUserPronouns(this.id, pronouns);
    }

    public void delete() {
        this.dao.deleteUser(this.id);
    }
}
