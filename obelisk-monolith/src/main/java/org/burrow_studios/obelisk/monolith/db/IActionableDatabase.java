package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.entity.discord.*;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.*;
import org.burrow_studios.obelisk.monolith.action.entity.project.*;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.*;
import org.burrow_studios.obelisk.monolith.action.entity.user.*;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IActionableDatabase {
    default List<BackendUser> onUserList(@NotNull DatabaseUserListAction action) throws DatabaseException { return null; }
    default BackendUser onUserGet(@NotNull DatabaseUserGetAction action) throws DatabaseException { return null; }
    default BackendUser onUserBuild(@NotNull DatabaseUserBuilder builder) throws DatabaseException { return null; }
    default void onUserModify(@NotNull DatabaseUserModifier modifier) throws DatabaseException { }
    default void onUserDelete(@NotNull DatabaseUserDeleteAction deleteAction) throws DatabaseException { }

    default List<BackendTicket> onTicketList(@NotNull DatabaseTicketListAction action) throws DatabaseException { return null; }
    default BackendTicket onTicketGet(@NotNull DatabaseTicketGetAction action) throws DatabaseException { return null; }
    default BackendTicket onTicketBuild(@NotNull DatabaseTicketBuilder builder) throws DatabaseException { return null; }
    default void onTicketModify(@NotNull DatabaseTicketModifier modifier) throws DatabaseException { }
    default void onTicketDelete(@NotNull DatabaseTicketDeleteAction deleteAction) throws DatabaseException { }
    default void onTicketUserAdd(@NotNull DatabaseTicketUserAddAction action) throws DatabaseException { }
    default void onTicketUserRemove(@NotNull DatabaseTicketUserRemoveAction action) throws DatabaseException { }

    default List<BackendProject> onProjectList(@NotNull DatabaseProjectListAction action) throws DatabaseException { return null; }
    default BackendProject onProjectGet(@NotNull DatabaseProjectGetAction action) throws DatabaseException { return null; }
    default BackendProject onProjectBuild(@NotNull DatabaseProjectBuilder builder) throws DatabaseException { return null; }
    default void onProjectModify(@NotNull DatabaseProjectModifier modifier) throws DatabaseException { }
    default void onProjectDelete(@NotNull DatabaseProjectDeleteAction deleteAction) throws DatabaseException { }
    default void onProjectUserAdd(@NotNull DatabaseProjectUserAddAction action) throws DatabaseException { }
    default void onProjectUserRemove(@NotNull DatabaseProjectUserRemoveAction action) throws DatabaseException { }

    default List<BackendDiscordAccount> onDiscordAccountList(@NotNull DatabaseDiscordAccountListAction action) throws DatabaseException { return null; }
    default BackendDiscordAccount onDiscordAccountGet(@NotNull DatabaseDiscordAccountGetAction action) throws DatabaseException { return null; }
    default BackendDiscordAccount onDiscordAccountBuild(@NotNull DatabaseDiscordAccountBuilder builder) throws DatabaseException { return null; }
    default void onDiscordAccountModify(@NotNull DatabaseDiscordAccountModifier modifier) throws DatabaseException { }
    default void onDiscordAccountDelete(@NotNull DatabaseDiscordAccountDeleteAction deleteAction) throws DatabaseException { }

    default List<BackendMinecraftAccount> onMinecraftAccountList(@NotNull DatabaseMinecraftAccountListAction action) throws DatabaseException { return null; }
    default BackendMinecraftAccount onMinecraftAccountGet(@NotNull DatabaseMinecraftAccountGetAction action) throws DatabaseException { return null; }
    default BackendMinecraftAccount onMinecraftAccountBuild(@NotNull DatabaseMinecraftAccountBuilder builder) throws DatabaseException { return null; }
    default void onMinecraftAccountModify(@NotNull DatabaseMinecraftAccountModifier modifier) throws DatabaseException { }
    default void onMinecraftAccountDelete(@NotNull DatabaseMinecraftAccountDeleteAction deleteAction) throws DatabaseException { }
}
