package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.jetbrains.annotations.NotNull;

public class DatabaseMinecraftAccountGetAction extends DatabaseGetAction<BackendMinecraftAccount> {
    public DatabaseMinecraftAccountGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
