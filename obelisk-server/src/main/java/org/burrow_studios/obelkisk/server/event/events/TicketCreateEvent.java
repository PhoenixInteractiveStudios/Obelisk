package org.burrow_studios.obelkisk.server.event.events;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.event.AvailableAt;
import org.burrow_studios.obelkisk.server.event.Event;
import org.burrow_studios.obelkisk.server.event.ExecutionStage;
import org.jetbrains.annotations.NotNull;

public final class TicketCreateEvent implements Event {
    // BEFORE EXECUTION
    private final IReplyCallback interaction;
    private final DiscordAccount discordAccount;
    private final User author;

    // AFTER EXECUTION
    private Ticket ticket;
    private TextChannel channel;

    public TicketCreateEvent(@NotNull IReplyCallback interaction, @NotNull DiscordAccount discordAccount, @NotNull User author) {
        this.interaction = interaction;
        this.discordAccount = discordAccount;
        this.author = author;
    }

    public @NotNull DiscordAccount getDiscordAccount() {
        return this.discordAccount;
    }

    public @NotNull User getAuthor() {
        return this.author;
    }

    public @NotNull IReplyCallback getInteraction() {
        return this.interaction;
    }

    @AvailableAt(ExecutionStage.MONITOR)
    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(@NotNull Ticket ticket) {
        this.ticket = ticket;
    }

    @AvailableAt(ExecutionStage.MONITOR)
    public TextChannel getChannel() {
        return this.channel;
    }

    public void setChannel(@NotNull TextChannel channel) {
        this.channel = channel;
    }
}
