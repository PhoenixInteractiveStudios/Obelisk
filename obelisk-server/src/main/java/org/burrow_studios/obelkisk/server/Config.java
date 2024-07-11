package org.burrow_studios.obelkisk.server;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public record Config(
        String token,
        long ticketCreateChannel,
        long ticketCategory,
        long superuserRole,
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

        String superuserRoleStr = properties.getProperty("role.superuser");
        if (superuserRoleStr == null)
            throw new IllegalArgumentException("role.superuser may not be null");
        long superuserRole;
        try {
            superuserRole = Long.parseLong(superuserRoleStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("role.superuser must be a valid id", e);
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

        return new Config(token, ticketCreateChannel, ticketCategory, superuserRole, moderationRole);
    }
}
