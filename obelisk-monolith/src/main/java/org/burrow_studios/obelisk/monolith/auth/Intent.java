package org.burrow_studios.obelisk.monolith.auth;

import org.jetbrains.annotations.NotNull;

public record Intent(
        long id,
        @NotNull String name,
        String description
) { }
