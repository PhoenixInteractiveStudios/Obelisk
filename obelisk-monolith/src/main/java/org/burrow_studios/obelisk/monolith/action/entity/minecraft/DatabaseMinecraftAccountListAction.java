package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.monolith.action.DatabaseListAction;
import org.jetbrains.annotations.NotNull;

public class DatabaseMinecraftAccountListAction extends DatabaseListAction<AbstractMinecraftAccount> {
    public DatabaseMinecraftAccountListAction(@NotNull EntityCache<AbstractMinecraftAccount> cache) {
        super(cache);
    }
}
