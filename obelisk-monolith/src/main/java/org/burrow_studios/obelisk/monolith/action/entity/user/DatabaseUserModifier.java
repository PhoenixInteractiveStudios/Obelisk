package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.action.DatabaseModifier;
import org.burrow_studios.obelisk.monolith.entities.BackendUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseUserModifier extends DatabaseModifier<User> implements UserModifier {
    private String name;

    public DatabaseUserModifier(@NotNull BackendUser user) {
        super(user);
    }

    @Override
    public @NotNull DatabaseUserModifier setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public @Nullable String getName() {
        return this.name;
    }
}
