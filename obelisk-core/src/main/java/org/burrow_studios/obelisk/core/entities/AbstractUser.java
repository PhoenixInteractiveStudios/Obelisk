package org.burrow_studios.obelisk.core.entities;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.core.cache.OrderedEntitySetView;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractUser extends AbstractEntity implements User {
    private @NotNull String name;
    // FIXME: possible circular dependency
    private final @NotNull OrderedEntitySetView<AbstractDiscordAccount> discordAccounts;
    private final @NotNull OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts;

    protected AbstractUser(
            @NotNull AbstractObelisk obelisk,
            long id,
            @NotNull String name,
            @NotNull OrderedEntitySetView<AbstractDiscordAccount> discordAccounts,
            @NotNull OrderedEntitySetView<AbstractMinecraftAccount> minecraftAccounts
    ) {
        super(obelisk, id);
        this.name = name;
        this.discordAccounts = discordAccounts;
        this.minecraftAccounts = minecraftAccounts;
    }

    @Override
    public final @NotNull String getName() {
        return this.name;
    }

    public final void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public final @NotNull OrderedEntitySetView<AbstractDiscordAccount> getDiscordAccounts() {
        return this.discordAccounts;
    }

    @Override
    public final @NotNull OrderedEntitySetView<AbstractMinecraftAccount> getMinecraftAccounts() {
        return this.minecraftAccounts;
    }
}
