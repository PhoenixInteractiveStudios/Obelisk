package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.monolith.action.entity.discord.*;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.*;
import org.burrow_studios.obelisk.monolith.action.entity.project.*;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.*;
import org.burrow_studios.obelisk.monolith.action.entity.user.*;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.List;

public interface IActionableDatabase extends Closeable {
    default List<BackendUser> getUsers(@NotNull DatabaseUserListAction action) throws DatabaseException { return null; }
    default BackendUser getUser(@NotNull DatabaseUserGetAction action) throws DatabaseException { return null; }
    default BackendUser createUser(@NotNull DatabaseUserBuilder builder) throws DatabaseException { return null; }
    default void modifyUser(@NotNull DatabaseUserModifier modifier) throws DatabaseException { }
    default void deleteUser(@NotNull DatabaseUserDeleteAction deleteAction) throws DatabaseException { }

    default List<BackendTicket> getTickets(@NotNull DatabaseTicketListAction action) throws DatabaseException { return null; }
    default BackendTicket getTicket(@NotNull DatabaseTicketGetAction action) throws DatabaseException { return null; }
    default BackendTicket createTicket(@NotNull DatabaseTicketBuilder builder) throws DatabaseException { return null; }
    default void modifyTicket(@NotNull DatabaseTicketModifier modifier) throws DatabaseException { }
    default void deleteTicket(@NotNull DatabaseTicketDeleteAction deleteAction) throws DatabaseException { }
    default void addTicketUser(@NotNull DatabaseTicketUserAddAction action) throws DatabaseException { }
    default void removeTicketUser(@NotNull DatabaseTicketUserRemoveAction action) throws DatabaseException { }

    default List<BackendProject> getProjects(@NotNull DatabaseProjectListAction action) throws DatabaseException { return null; }
    default BackendProject getProject(@NotNull DatabaseProjectGetAction action) throws DatabaseException { return null; }
    default BackendProject createProject(@NotNull DatabaseProjectBuilder builder) throws DatabaseException { return null; }
    default void modifyProject(@NotNull DatabaseProjectModifier modifier) throws DatabaseException { }
    default void deleteProject(@NotNull DatabaseProjectDeleteAction deleteAction) throws DatabaseException { }
    default void addProjectMember(@NotNull DatabaseProjectUserAddAction action) throws DatabaseException { }
    default void removeProjectMember(@NotNull DatabaseProjectUserRemoveAction action) throws DatabaseException { }

    default List<BackendDiscordAccount> getDiscordAccounts(@NotNull DatabaseDiscordAccountListAction action) throws DatabaseException { return null; }
    default BackendDiscordAccount getDiscordAccount(@NotNull DatabaseDiscordAccountGetAction action) throws DatabaseException { return null; }
    default BackendDiscordAccount createDiscordAccount(@NotNull DatabaseDiscordAccountBuilder builder) throws DatabaseException { return null; }
    default void modifyDiscordAccount(@NotNull DatabaseDiscordAccountModifier modifier) throws DatabaseException { }
    default void deleteDiscordAccount(@NotNull DatabaseDiscordAccountDeleteAction deleteAction) throws DatabaseException { }

    default List<BackendMinecraftAccount> getMinecraftAccounts(@NotNull DatabaseMinecraftAccountListAction action) throws DatabaseException { return null; }
    default BackendMinecraftAccount getMinecraftAccount(@NotNull DatabaseMinecraftAccountGetAction action) throws DatabaseException { return null; }
    default BackendMinecraftAccount createMinecraftAccount(@NotNull DatabaseMinecraftAccountBuilder builder) throws DatabaseException { return null; }
    default void modifyMinecraftAccount(@NotNull DatabaseMinecraftAccountModifier modifier) throws DatabaseException { }
    default void deleteMinecraftAccount(@NotNull DatabaseMinecraftAccountDeleteAction deleteAction) throws DatabaseException { }
}
