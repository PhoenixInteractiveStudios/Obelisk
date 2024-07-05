package org.burrow_studios.obelkisk.server.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.dao.DiscordAccountDAO;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountListener extends ListenerAdapter {
    private final Obelisk obelisk;

    public DiscordAccountListener(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    private @NotNull DiscordAccount provideDiscordAccount(long snowflake, @NotNull String name) {
        DiscordAccountDAO dao = this.obelisk.getDiscordAccountDAO();

        return dao.getDiscordAccount(snowflake)
                .orElseGet(() -> dao.createDiscordAccount(snowflake, name));
    }

    private void iterateAll(@NotNull JDA jda) {
        jda.getUsers().forEach(user -> {
            this.provideDiscordAccount(user.getIdLong(), user.getName());
        });
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.iterateAll(event.getJDA());
    }

    @Override
    public void onSessionResume(@NotNull SessionResumeEvent event) {
        this.iterateAll(event.getJDA());
    }

    @Override
    public void onSessionRecreate(@NotNull SessionRecreateEvent event) {
        this.iterateAll(event.getJDA());
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        DiscordAccount discordAccount = this.provideDiscordAccount(event.getUser().getIdLong(), event.getNewName());

        discordAccount.setName(event.getNewName());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        this.provideDiscordAccount(event.getUser().getIdLong(), event.getUser().getName());
    }
}
