package org.burrow_studios.obelisk.monolith;

import org.burrow_studios.obelisk.core.AbstractObelisk;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserBuilder;
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
    public @NotNull DatabaseUserBuilder createUser() {
        return new DatabaseUserBuilder(this);
    }

    @Override
    public @NotNull DatabaseTicketBuilder createTicket() {
        return new DatabaseTicketBuilder(this);
    }

    @Override
    public @NotNull DatabaseProjectBuilder createProject() {
        return new DatabaseProjectBuilder(this);
    }

    @Override
    public @NotNull DatabaseDiscordAccountBuilder createDiscordAccount() {
        return new DatabaseDiscordAccountBuilder(this);
    }

    @Override
    public @NotNull DatabaseMinecraftAccountBuilder createMinecraftAccount() {
        return new DatabaseMinecraftAccountBuilder(this);
    }
}
