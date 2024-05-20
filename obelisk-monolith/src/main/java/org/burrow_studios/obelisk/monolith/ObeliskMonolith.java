package org.burrow_studios.obelisk.monolith;

import org.burrow_studios.obelisk.api.action.entity.discord.MinecraftAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.minecraft.DiscordAccountBuilder;
import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.action.entity.ticket.TicketBuilder;
import org.burrow_studios.obelisk.api.action.entity.user.UserBuilder;
import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.monolith.db.DatabaseAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObeliskMonolith extends AbstractObelisk {
    private static final Logger LOG = LoggerFactory.getLogger(ObeliskMonolith.class);

    private final DatabaseAdapter databaseAdapter;

    public ObeliskMonolith() {
        super();

        this.databaseAdapter = new DatabaseAdapter();
    }

    public @NotNull DatabaseAdapter getDatabaseAdapter() {
        return this.databaseAdapter;
    }

    @Override
    public @NotNull UserBuilder createUser() {
        // TODO
        return null;
    }

    @Override
    public @NotNull TicketBuilder createTicket() {
        // TODO
        return null;
    }

    @Override
    public @NotNull ProjectBuilder createProject() {
        // TODO
        return null;
    }

    @Override
    public @NotNull DiscordAccountBuilder createDiscordAccount() {
        // TODO
        return null;
    }

    @Override
    public @NotNull MinecraftAccountBuilder createMinecraftAccount() {
        // TODO
        return null;
    }
}
