package org.burrow_studios.obelisk.core.source;

import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.jetbrains.annotations.NotNull;

public interface EventProvider {
    @NotNull ObeliskImpl getAPI();
}
