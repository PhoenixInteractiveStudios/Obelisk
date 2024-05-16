package org.burrow_studios.obelisk.client.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.MinecraftAccount;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.client.action.DeleteActionImpl;
import org.burrow_studios.obelisk.client.action.entity.minecraft.MinecraftAccountModifierImpl;
import org.burrow_studios.obelisk.core.entities.AbstractMinecraftAccount;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MinecraftAccountImpl extends AbstractMinecraftAccount {
    public MinecraftAccountImpl(
            @NotNull ObeliskImpl obelisk,
            long id,
            @NotNull UUID uuid,
            @NotNull String cachedName,
            @NotNull AbstractUser user
    ) {
        super(obelisk, id, uuid, cachedName, user);
    }

    @Override
    public @NotNull MinecraftAccountModifierImpl modify() {
        return new MinecraftAccountModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<MinecraftAccount> delete() {
        Route.Compiled route = Route.Minecraft.DELETE_MINECRAFT_ACCOUNT.compile(this.id);

        return new DeleteActionImpl<>(((ObeliskImpl) this.getAPI()), route, this.getId(), MinecraftAccount.class);
    }
}
