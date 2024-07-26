package org.burrow_studios.obelkisk.ticket;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import org.burrow_studios.obelkisk.entity.Ticket;
import org.burrow_studios.obelkisk.entity.User;
import org.burrow_studios.obelkisk.Obelisk;
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

        Role publicRole = category.getGuild().getPublicRole();

        TextChannel channel;
        try {
            channel = category.createTextChannel("ticket")
                    // prevent @everyone from seeing the channel
                    .addPermissionOverride(publicRole, 0, Permission.VIEW_CHANNEL.getRawValue())
                    // allow author to see the channel
                    .addMemberPermissionOverride(author.getSnowflake(), Permission.VIEW_CHANNEL.getRawValue(), 0)
                    // allow moderators to see the channel
                    .addRolePermissionOverride(this.obelisk.getConfig().moderationRole(), Permission.VIEW_CHANNEL.getRawValue(), 0)
                    .complete();
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

    public void closeTicket(@NotNull JDA jda, @NotNull Ticket ticket) {
        TextChannel channel = jda.getTextChannelById(ticket.getChannelId());

        if (channel == null)
            throw new IllegalStateException("Ticket channel does not exist");

        long archiveId = this.obelisk.getConfig().ticketArchive();
        Category archive = jda.getCategoryById(archiveId);

        if (archive == null)
            throw new IllegalStateException("Ticket archive does not exist");

        TextChannelManager manager = channel.getManager();

        // remove user overrides
        for (User user : ticket.getUsers())
            manager = manager.removePermissionOverride(user.getSnowflake());

        // remove moderator override
        manager = manager.removePermissionOverride(this.obelisk.getConfig().moderationRole());

        // move to archive
        manager = manager.setParent(archive);

        manager.queue();
    }
}
