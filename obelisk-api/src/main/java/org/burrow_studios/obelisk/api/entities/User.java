package org.burrow_studios.obelisk.api.entities;

import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.action.entity.user.UserModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * A User is a member of the community, part of the team or moderation or a bot. Users can have zero or more Discord and
 * Minecraft accounts linked to them (exclusively). A custom name can be set by the user themselves.
 * @see Group
 * @see Ticket
 */
public interface User extends Turtle {
    @Override
    @NotNull UserModifier modify();

    @Override
    @NotNull DeleteAction<User> delete();

    /**
     * Provides the name of this User. Usernames are not guaranteed to be unique and cen be set by the User themselves.
     * @return The username.
     */
    @NotNull String getName();

    /**
     * Provides a list of snowflake ids that each represent a Discord user this User is linked to (exclusively).
     * @return List of snowflake ids.
     */
    @NotNull List<Long> getDiscordIds();

    /**
     * Provides a list of {@link UUID UUIDs} that each represent a Minecraft account this User is linked to (exclusively).
     * @return List of {@link UUID UUIDs}.
     */
    @NotNull List<UUID> getMinecraftIds();
}
