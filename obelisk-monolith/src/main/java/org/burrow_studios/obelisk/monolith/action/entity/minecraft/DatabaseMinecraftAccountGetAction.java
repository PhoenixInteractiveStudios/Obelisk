package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseGetAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseMinecraftAccountGetAction extends DatabaseGetAction<MinecraftAccount> {
    public DatabaseMinecraftAccountGetAction(@NotNull ObeliskMonolith obelisk, long id) {
        super(obelisk, id);
    }
}
