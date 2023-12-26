package org.burrow_studios.obelisk.api;

import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

public class ObeliskBuilder {
    public @NotNull Obelisk build() throws IllegalArgumentException {
        return new ObeliskImpl();
    }
}
