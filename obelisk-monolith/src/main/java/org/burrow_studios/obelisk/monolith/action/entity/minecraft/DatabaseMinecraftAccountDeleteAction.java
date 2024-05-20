package org.burrow_studios.obelisk.monolith.action.entity.minecraft;

import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseDeleteAction;
import org.burrow_studios.obelisk.monolith.entities.BackendMinecraftAccount;
import org.jetbrains.annotations.NotNull;

public class DatabaseMinecraftAccountDeleteAction extends DatabaseDeleteAction<MinecraftAccount> {
    public DatabaseMinecraftAccountDeleteAction(@NotNull BackendMinecraftAccount minecraftAccount) {
        super(((ObeliskMonolith) minecraftAccount.getAPI()), minecraftAccount.getId(), MinecraftAccount.class);
    }
}
