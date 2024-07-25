package org.burrow_studios.obelkisk.server.ticket;

import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.burrow_studios.obelisk.api.entity.User;
import org.burrow_studios.obelisk.api.entity.Ticket;
import org.burrow_studios.obelkisk.server.Obelisk;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TicketManager {
    private final Obelisk obelisk;

    public TicketManager(@NotNull Obelisk obelisk) {
        this.obelisk = obelisk;
    }

    // interaction should already be filtered (this has to be an actual ticket create interaction)
    public void createTicket(@NotNull IReplyCallback interaction) {
        // create user
        User author = this.obelisk.getUserDAO().getUser(interaction.getUser().getIdLong())
                .orElseGet(() -> this.obelisk.getUserDAO().createUser(interaction.getUser().getIdLong(), interaction.getUser().getName()));



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

        TextChannel channel;
        try {
            channel = category.createTextChannel("ticket").complete();
        } catch (RuntimeException e) {
            String errorMsg = this.obelisk.getTextProvider().get("ticket.create.error");
            interaction.getHook().sendMessage(errorMsg).queue();

            throw new RuntimeException("Failed to create channel for new ticket", e);
        }

        Ticket ticket = this.obelisk.getTicketDAO().createTicket(channel.getIdLong());
        ticket.addUser(author);

        String successMsg = this.obelisk.getTextProvider().get("ticket.create.success", "channel", channel.getAsMention());
        interaction.getHook().editOriginal(successMsg).queue();



        String welcomeMsg = this.obelisk.getTextProvider().get("ticket.create.welcome", "user", interaction.getUser().getAsMention());

        channel.getManager().setName("ticket-" + ticket.getId()).queue();
        channel.sendMessage(welcomeMsg).queueAfter(1, TimeUnit.SECONDS);
    }
}
