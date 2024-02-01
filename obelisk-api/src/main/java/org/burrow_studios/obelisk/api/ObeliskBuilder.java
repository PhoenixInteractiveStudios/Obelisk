package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.core.ObeliskBuilderImpl;
import org.jetbrains.annotations.NotNull;

public interface ObeliskBuilder {
    static @NotNull ObeliskBuilder create() {
        return new ObeliskBuilderImpl();
    }

    @NotNull ObeliskBuilder setToken(String token);

    @NotNull Obelisk build() throws IllegalArgumentException;
}
