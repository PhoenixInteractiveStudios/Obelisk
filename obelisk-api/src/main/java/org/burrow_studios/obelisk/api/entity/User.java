package org.burrow_studios.obelisk.api.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface User {
    long getId();
    @NotNull String getName();
    @Nullable String getPronouns();

    void setName(@NotNull String name);
    void setPronouns(@Nullable String pronouns);

    void delete();
}
