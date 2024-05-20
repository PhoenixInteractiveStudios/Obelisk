package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountModifier;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountModifier;
import org.burrow_studios.obelisk.monolith.action.entity.project.*;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.*;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.user.DatabaseUserModifier;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

public interface IActionableDatabase {
    default BackendUser onUserGet(@NotNull DatabaseUserGetAction action) throws DatabaseException { return null; }
    default BackendUser onUserBuild(@NotNull DatabaseUserBuilder builder) throws DatabaseException { return null; }
    default void onUserModify(@NotNull DatabaseUserModifier modifier) throws DatabaseException { }
    default void onUserDelete(@NotNull DatabaseUserDeleteAction deleteAction) throws DatabaseException { }

    default BackendTicket onTicketGet(@NotNull DatabaseTicketGetAction action) throws DatabaseException { return null; }
    default BackendTicket onTicketBuild(@NotNull DatabaseTicketBuilder builder) throws DatabaseException { return null; }
    default void onTicketModify(@NotNull DatabaseTicketModifier modifier) throws DatabaseException { }
    default void onTicketDelete(@NotNull DatabaseTicketDeleteAction deleteAction) throws DatabaseException { }
    default void onTicketUserAdd(@NotNull DatabaseTicketUserAddAction action) throws DatabaseException { }
    default void onTicketUserRemove(@NotNull DatabaseTicketUserRemoveAction action) throws DatabaseException { }

    default BackendProject onProjectGet(@NotNull DatabaseProjectGetAction action) throws DatabaseException { return null; }
    default BackendProject onProjectBuild(@NotNull DatabaseProjectBuilder builder) throws DatabaseException { return null; }
    default void onProjectModify(@NotNull DatabaseProjectModifier modifier) throws DatabaseException { }
    default void onProjectDelete(@NotNull DatabaseProjectDeleteAction deleteAction) throws DatabaseException { }
    default void onProjectUserAdd(@NotNull DatabaseProjectUserAddAction action) throws DatabaseException { }
    default void onProjectUserRemove(@NotNull DatabaseProjectUserRemoveAction action) throws DatabaseException { }

    default BackendDiscordAccount onDiscordAccountGet(@NotNull DatabaseDiscordAccountGetAction action) throws DatabaseException { return null; }
    default BackendDiscordAccount onDiscordAccountBuild(@NotNull DatabaseDiscordAccountBuilder builder) throws DatabaseException { return null; }
    default void onDiscordAccountModify(@NotNull DatabaseDiscordAccountModifier modifier) throws DatabaseException { }
    default void onDiscordAccountDelete(@NotNull DatabaseDiscordAccountDeleteAction deleteAction) throws DatabaseException { }

    default BackendMinecraftAccount onMinecraftAccountGet(@NotNull DatabaseMinecraftAccountGetAction action) throws DatabaseException { return null; }
    default BackendMinecraftAccount onMinecraftAccountBuild(@NotNull DatabaseMinecraftAccountBuilder builder) throws DatabaseException { return null; }
    default void onMinecraftAccountModify(@NotNull DatabaseMinecraftAccountModifier modifier) throws DatabaseException { }
    default void onMinecraftAccountDelete(@NotNull DatabaseMinecraftAccountDeleteAction deleteAction) throws DatabaseException { }
}
