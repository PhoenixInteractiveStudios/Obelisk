package org.burrow_studios.obelisk.core.source;

import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.jetbrains.annotations.NotNull;

public interface DataProvider {
    @NotNull ObeliskImpl getAPI();

    @NotNull Request submitRequest(@NotNull ActionImpl<?> action);
}
