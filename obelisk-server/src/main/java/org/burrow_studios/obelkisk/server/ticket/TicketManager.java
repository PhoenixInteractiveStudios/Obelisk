package org.burrow_studios.obelkisk.server.ticket;

import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.burrow_studios.obelkisk.server.event.EventHandler;
import org.burrow_studios.obelkisk.server.event.EventListener;
import org.burrow_studios.obelkisk.server.event.ExecutionStage;
import org.burrow_studios.obelkisk.server.event.events.TicketCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TicketManager implements EventListener {
    private final Obelisk obelisk;

    public TicketManager(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
        this.obelisk.getEventManager().registerListener(this);
    }

    // interaction should already be filtered (this has to be an actual ticket create interaction)
    public void createTicket(@NotNull IReplyCallback interaction) {
        // create user
        DiscordAccount discordAccount = this.obelisk.getDiscordAccountDAO().getDiscordAccount(interaction.getUser().getIdLong())
                .orElseGet(() -> this.obelisk.getDiscordAccountDAO().createDiscordAccount(interaction.getUser().getIdLong(), interaction.getUser().getName()));
        User u = discordAccount.getUser();
        if (u == null)
            u = this.obelisk.getUserDAO().createUser(discordAccount.getName(), null);
        final User user = u;

        this.obelisk.getEventManager().handle(new TicketCreateEvent(interaction, discordAccount, user));
    }

    @EventHandler(stage = ExecutionStage.EXECUTE)
    public void createTicket(@NotNull TicketCreateEvent event) {
        final long     categoryId = this.obelisk.getConfig().ticketCategory();
        final Category category   = event.getInteraction().getJDA().getCategoryById(categoryId);

        if (category == null) {
            String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");

            event.getInteraction().getHook()
                    .setEphemeral(true)
                    .sendMessage(errorMsg)
                    .queue();
            return;
        }

        event.getInteraction().deferReply(true).queue();

        TextChannel channel;
        try {
            channel = category.createTextChannel("ticket").complete();
        } catch (RuntimeException e) {
            String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");
            event.getInteraction().getHook().sendMessage(errorMsg).queue();

            throw new RuntimeException("Failed to create channel for new ticket", e);
        }
        event.setChannel(channel);

        Ticket ticket = this.obelisk.getTicketDAO().createTicket(channel.getIdLong());
        ticket.addUser(event.getAuthor());
        event.setTicket(ticket);

        event.getInteraction().getHook().deleteOriginal().queue();
    }

    @EventHandler(stage = ExecutionStage.MONITOR)
    public void onCreate(@NotNull TicketCreateEvent event) {
        String welcomeMsg = this.obelisk.getTextProvider().get("ticket.create.welcome", "user", event.getInteraction().getUser().getAsMention());

        event.getChannel().getManager().setName("ticket-" + event.getTicket().getId()).queue();
        event.getChannel().sendMessage(welcomeMsg).queueAfter(1, TimeUnit.SECONDS);
    }
}
