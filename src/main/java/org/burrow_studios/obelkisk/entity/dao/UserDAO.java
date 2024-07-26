package org.burrow_studios.obelkisk.entity.dao;

import org.burrow_studios.obelkisk.entity.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface UserDAO {
    @NotNull User createUser(long snowflake, @NotNull String name);

    @NotNull List<? extends User> listUsers();
    @NotNull Optional<User> getUser(long snowflake);
    @NotNull String getUserName(long snowflake);

    void setUserName(long snowflake, @NotNull String name);

    void deleteUser(long snowflake);
}
