package org.burrow_studios.obelisk.api.entity.dao;

import org.burrow_studios.obelisk.api.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    @NotNull User createUser(@NotNull String name, @Nullable String pronouns);

    @NotNull List<? extends User> listUsers();
    @NotNull Optional<User> getUser(long id);
    @NotNull String getUserName(long id);
    @Nullable String getUserPronouns(long id);

    void setUserName(long id, @NotNull String name);
    void setUserPronouns(long id, @Nullable String pronouns);

    void deleteUser(long id);
}
