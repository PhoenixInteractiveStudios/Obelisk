package org.burrow_studios.obelkisk.server.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.burrow_studios.obelkisk.server.db.interfaces.DiscordAccountDB;
import org.burrow_studios.obelkisk.server.entity.DatabaseDiscordAccount;
import org.burrow_studios.obelkisk.server.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountListener extends ListenerAdapter {
    private final Obelisk obelisk;

    public DiscordAccountListener(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    private @NotNull DatabaseDiscordAccount provideDiscordAccount(long snowflake, @NotNull String name) {
        DiscordAccountDB database = this.obelisk.getDiscordAccountDB();

        try {
            return database.getDiscordAccount(snowflake);
        } catch (NoSuchEntryException e) {
            return database.createDiscordAccount(snowflake, name);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getUsers().forEach(user -> {
            this.provideDiscordAccount(user.getIdLong(), user.getName());
        });
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        DiscordAccount discordAccount = this.provideDiscordAccount(event.getUser().getIdLong(), event.getNewName());

        discordAccount.setName(event.getNewName());
    }
}
