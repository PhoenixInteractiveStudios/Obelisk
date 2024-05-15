package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.Obelisk;
import org.jetbrains.annotations.NotNull;

public interface IEntity {
    @NotNull Obelisk getAPI();

    long getId();
}
