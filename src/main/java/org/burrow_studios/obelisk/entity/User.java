package org.burrow_studios.obelisk.entity;

import org.burrow_studios.obelisk.entity.dao.UserDAO;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class User {
    private final long snowflake;
    private final UserDAO dao;

    public User(long snowflake, @NotNull UserDAO dao) {
        this.snowflake = snowflake;
        this.dao = dao;
    }

    public long getSnowflake() {
        return this.snowflake;
    }

    public @NotNull String getName() {
        return this.dao.getUserName(this.snowflake);
    }

    public void setName(@NotNull String name) {
        this.dao.setUserName(this.snowflake, name);
    }

    public void delete() {
        this.dao.deleteUser(this.snowflake);
    }
}
