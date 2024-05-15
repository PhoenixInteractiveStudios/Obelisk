package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.Obelisk;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.Modifier;
import org.jetbrains.annotations.NotNull;

public interface IEntity {
    @NotNull Obelisk getAPI();

    long getId();

    @NotNull Modifier<? extends IEntity> modify();

    @NotNull DeleteAction<? extends IEntity> delete();
}
