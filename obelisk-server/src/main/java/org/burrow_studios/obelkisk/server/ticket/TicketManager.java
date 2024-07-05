package org.burrow_studios.obelkisk.server.ticket;

import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.entity.DiscordAccount;
import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TicketManager {
    private static final Logger LOG = LoggerFactory.getLogger(TicketManager.class);

    private final Obelisk obelisk;

    public TicketManager(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    // interaction should already be filtered (this has to be an actual ticket create interaction)
    public void createTicket(@NotNull IReplyCallback interaction) {
        final long     categoryId = this.obelisk.getConfig().ticketCategory();
        final Category category   = interaction.getJDA().getCategoryById(categoryId);

        if (category == null) {
            String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");

            interaction.getHook()
                    .setEphemeral(true)
                    .sendMessage(errorMsg)
                    .queue();
            return;
        }

        interaction.deferReply(true).queue();

        // create user
        DiscordAccount discordAccount = this.obelisk.getDiscordAccountDAO().getDiscordAccount(interaction.getUser().getIdLong());
        User u = discordAccount.getUser();
        if (u == null)
            u = this.obelisk.getUserDAO().createUser(discordAccount.getName(), null);
        final User user = u;

        // create ticket channel
        category.createTextChannel("ticket")
                .queue(channel -> {
                    Ticket ticket = this.obelisk.getTicketDAO().createTicket(channel.getIdLong());
                    ticket.addUser(user);

                    interaction.getHook().deleteOriginal().queue();

                    String welcomeMsg = this.obelisk.getTextProvider().get("ticket.create.welcome", "user", interaction.getUser().getAsMention());

                    channel.getManager().setName("ticket-" + ticket.getId()).queue();
                    channel.sendMessage(welcomeMsg).queueAfter(1, TimeUnit.SECONDS);
                }, throwable -> {
                    String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");

                    interaction.getHook().sendMessage(errorMsg).queue();

                    LOG.warn("Failed to create channel for new ticket", throwable);
                });
    }
}
