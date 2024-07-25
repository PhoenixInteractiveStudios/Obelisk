package org.burrow_studios.obelkisk.server.listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelisk.api.entity.dao.UserDAO;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;

public class DiscordAccountListener extends ListenerAdapter {
    private final Obelisk obelisk;

    public DiscordAccountListener(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    private @NotNull User provideDiscordAccount(long snowflake, @NotNull String name) {
        UserDAO dao = this.obelisk.getUserDAO();

        return dao.getUser(snowflake)
                .orElseGet(() -> dao.createUser(snowflake, name));
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
        User user = this.provideDiscordAccount(event.getUser().getIdLong(), event.getNewName());

        user.setName(event.getNewName());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        this.provideDiscordAccount(event.getUser().getIdLong(), event.getUser().getName());
    }
}
