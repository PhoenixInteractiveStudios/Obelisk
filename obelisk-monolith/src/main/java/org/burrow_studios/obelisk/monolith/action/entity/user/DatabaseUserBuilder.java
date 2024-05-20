package org.burrow_studios.obelisk.monolith.action.entity.user;

import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseUserBuilder extends DatabaseBuilder<User> implements UserBuilder {
    private String name;

    public DatabaseUserBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }

    @Override
    public @NotNull DatabaseUserBuilder setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public @Nullable String getName() {
        return this.name;
    }
}
