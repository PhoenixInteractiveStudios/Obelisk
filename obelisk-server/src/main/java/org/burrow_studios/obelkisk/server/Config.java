package org.burrow_studios.obelkisk.server;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public record Config(
        String token,
        long ticketCreateChannel,
        long ticketCategory,
        long ticketArchive,
        long moderationRole
) {
    public static @NotNull Config fromFile(@NotNull File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        return fromProperties(properties);
    }

    public static @NotNull Config fromProperties(@NotNull Properties properties) {
        String token = properties.getProperty("token");
        if (token == null || token.isBlank() || token.equals("null"))
            throw new IllegalArgumentException("Token may not be null");

        String ticketCreateChannelStr = properties.getProperty("ticket.createChannel");
        if (ticketCreateChannelStr == null)
            throw new IllegalArgumentException("ticket.createChannel may not be null");
        long ticketCreateChannel;
        try {
            ticketCreateChannel = Long.parseLong(ticketCreateChannelStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ticket.createChannel must be a valid id", e);
        }

        String ticketCategoryStr = properties.getProperty("ticket.category");
        if (ticketCategoryStr == null)
            throw new IllegalArgumentException("ticket.category may not be null");
        long ticketCategory;
        try {
            ticketCategory = Long.parseLong(ticketCategoryStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ticket.category must be a valid id", e);
        }

        String ticketArchiveStr = properties.getProperty("ticket.archive");
        if (ticketArchiveStr == null)
            throw new IllegalArgumentException("ticket.archive may not be null");
        long ticketArchive;
        try {
            ticketArchive = Long.parseLong(ticketArchiveStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ticket.archive must be a valid id", e);
        }

        String moderationRoleStr = properties.getProperty("role.moderation");
        if (moderationRoleStr == null)
            throw new IllegalArgumentException("role.moderation may not be null");
        long moderationRole;
        try {
            moderationRole = Long.parseLong(moderationRoleStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("role.moderation must be a valid id", e);
        }

        return new Config(token, ticketCreateChannel, ticketCategory, ticketArchive, moderationRole);
    }

    public @NotNull Guild validate(@NotNull JDA jda) {
        Guild guild;

        TextChannel ticketCreateChannel = jda.getTextChannelById(this.ticketCreateChannel);
        if (ticketCreateChannel == null)
            throw new IllegalArgumentException("ticket.createChannel");
        guild = ticketCreateChannel.getGuild();

        Category ticketCategory = jda.getCategoryById(this.ticketCategory);
        if (ticketCategory == null)
            throw new IllegalArgumentException("ticket.category does not exist");
        if (!guild.equals(ticketCategory.getGuild()))
            throw new IllegalArgumentException("ticket.category does not match the expected guild");

        Category ticketArchive = jda.getCategoryById(this.ticketArchive);
        if (ticketArchive == null)
            throw new IllegalArgumentException("ticket.archive does not exist");
        if (!guild.equals(ticketArchive.getGuild()))
            throw new IllegalArgumentException("ticket.archive does not match the expected guild");

        Role moderationRole = jda.getRoleById(this.moderationRole);
        if (moderationRole == null)
            throw new IllegalArgumentException("role.moderation does not exist");
        if (!guild.equals(moderationRole.getGuild()))
            throw new IllegalArgumentException("role.moderation does not match the expected guild");

        return guild;
    }
}
