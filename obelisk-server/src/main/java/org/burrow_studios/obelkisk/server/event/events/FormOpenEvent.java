package org.burrow_studios.obelkisk.server.event.events;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.Form;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.event.AvailableAt;
import org.burrow_studios.obelkisk.server.event.Event;
import org.burrow_studios.obelkisk.server.event.ExecutionStage;
import org.jetbrains.annotations.NotNull;

public final class FormOpenEvent implements Event {
    // BEFORE EXECUTION
    private final IReplyCallback interaction;
    private final DiscordAccount discordAccount;
    private final String template;
    private final User user;

    // AFTER EXECUTION
    private Form form;
    private TextChannel channel;

    public FormOpenEvent(@NotNull IReplyCallback interaction, @NotNull DiscordAccount discordAccount, @NotNull String template, @NotNull User user) {
        this.interaction = interaction;
        this.discordAccount = discordAccount;
        this.template = template;
        this.user = user;
    }

    public @NotNull DiscordAccount getDiscordAccount() {
        return this.discordAccount;
    }

    public @NotNull User getUser() {
        return this.user;
    }

    public @NotNull IReplyCallback getInteraction() {
        return this.interaction;
    }

    public @NotNull String getTemplate() {
        return this.template;
    }

    @AvailableAt(ExecutionStage.MONITOR)
    public Form getForm() {
        return this.form;
    }

    public void setForm(@NotNull Form form) {
        this.form = form;
    }

    @AvailableAt(ExecutionStage.MONITOR)
    public TextChannel getChannel() {
        return this.channel;
    }

    public void setChannel(@NotNull TextChannel channel) {
        this.channel = channel;
    }
}
