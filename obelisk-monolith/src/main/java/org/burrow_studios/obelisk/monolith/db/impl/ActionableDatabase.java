package org.burrow_studios.obelisk.monolith.db.impl;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.action.entity.discord.*;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.*;
import org.burrow_studios.obelisk.monolith.action.entity.project.*;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.*;
import org.burrow_studios.obelisk.monolith.action.entity.user.*;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.burrow_studios.obelisk.util.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ActionableDatabase implements IActionableDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(ActionableDatabase.class);

    private final EntityDatabase entityDatabase;
    private final TurtleGenerator idGenerator = TurtleGenerator.get("db");

    public ActionableDatabase(@NotNull EntityDatabase entityDatabase) {
        this.entityDatabase = entityDatabase;
    }

    @Override
    public void close() throws IOException {
        LOG.warn("Shutting down");
        this.entityDatabase.close();
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - */

    @Override
    public List<BackendUser> getUsers(@NotNull DatabaseUserListAction action) throws DatabaseException {
        return this.entityDatabase.getUsers();
    }

    @Override
    public BackendUser getUser(@NotNull DatabaseUserGetAction action) throws DatabaseException {
        return this.entityDatabase.getUser(action.getId());
    }

    @Override
    public BackendUser createUser(@NotNull DatabaseUserBuilder builder) throws DatabaseException {
        final long id = this.idGenerator.newId();
        return this.entityDatabase.createUser(id, builder.getName());
    }

    @Override
    public void modifyUser(@NotNull DatabaseUserModifier modifier) throws DatabaseException {
        String oldName = modifier.getEntity().getName();
        String newName = modifier.getName();

        if (!Objects.equals(oldName, newName)) {
            this.entityDatabase.modifyUserName(modifier.getEntity().getId(), newName);
        }
    }

    @Override
    public void deleteUser(@NotNull DatabaseUserDeleteAction deleteAction) throws DatabaseException {
        this.entityDatabase.deleteUser(deleteAction.getId());
    }

    @Override
    public List<BackendTicket> getTickets(@NotNull DatabaseTicketListAction action) throws DatabaseException {
        return this.entityDatabase.getTickets();
    }

    @Override
    public BackendTicket getTicket(@NotNull DatabaseTicketGetAction action) throws DatabaseException {
        return this.entityDatabase.getTicket(action.getId());
    }

    @Override
    public BackendTicket createTicket(@NotNull DatabaseTicketBuilder builder) throws DatabaseException {
        final long id = this.idGenerator.newId();
        return this.entityDatabase.createTicket(id, builder.getTitle(), builder.getState());
    }

    @Override
    public void modifyTicket(@NotNull DatabaseTicketModifier modifier) throws DatabaseException {
        String oldTitle = modifier.getEntity().getTitle();
        String newTitle = modifier.getTitle();
        if (!Objects.equals(oldTitle, newTitle))
            this.entityDatabase.modifyTicketTitle(modifier.getEntity().getId(), newTitle);

        Ticket.State oldState = modifier.getEntity().getState();
        Ticket.State newState = modifier.getState();
        if (!Objects.equals(oldState, newState))
            this.entityDatabase.modifyTicketState(modifier.getEntity().getId(), newState);
    }

    @Override
    public void deleteTicket(@NotNull DatabaseTicketDeleteAction deleteAction) throws DatabaseException {
        this.entityDatabase.deleteTicket(deleteAction.getId());
    }

    @Override
    public void addTicketUser(@NotNull DatabaseTicketUserAddAction action) throws DatabaseException {
        this.entityDatabase.addTicketUser(action.getTicket().getId(), action.getUser().getId());
    }

    @Override
    public void removeTicketUser(@NotNull DatabaseTicketUserRemoveAction action) throws DatabaseException {
        this.entityDatabase.removeTicketUser(action.getTicket().getId(), action.getUser().getId());
    }

    @Override
    public List<BackendProject> getProjects(@NotNull DatabaseProjectListAction action) throws DatabaseException {
        return this.entityDatabase.getProjects();
    }

    @Override
    public BackendProject getProject(@NotNull DatabaseProjectGetAction action) throws DatabaseException {
        return this.entityDatabase.getProject(action.getId());
    }

    @Override
    public BackendProject createProject(@NotNull DatabaseProjectBuilder builder) throws DatabaseException {
        final long id = this.idGenerator.newId();
        return this.entityDatabase.createProject(id, builder.getTitle(), builder.getState());
    }

    @Override
    public void modifyProject(@NotNull DatabaseProjectModifier modifier) throws DatabaseException {
        String oldTitle = modifier.getEntity().getTitle();
        String newTitle = modifier.getTitle();
        if (!Objects.equals(oldTitle, newTitle))
            this.entityDatabase.modifyProjectTitle(modifier.getEntity().getId(), newTitle);

        Project.State oldState = modifier.getEntity().getState();
        Project.State newState = modifier.getState();
        if (!Objects.equals(oldState, newState))
            this.entityDatabase.modifyProjectState(modifier.getEntity().getId(), newState);
    }

    @Override
    public void deleteProject(@NotNull DatabaseProjectDeleteAction deleteAction) throws DatabaseException {
        this.entityDatabase.deleteProject(deleteAction.getId());
    }

    @Override
    public void addProjectMember(@NotNull DatabaseProjectUserAddAction action) throws DatabaseException {
        this.entityDatabase.addProjectMember(action.getProject().getId(), action.getUser().getId());
    }

    @Override
    public void removeProjectMember(@NotNull DatabaseProjectUserRemoveAction action) throws DatabaseException {
        this.entityDatabase.removeProjectMember(action.getProject().getId(), action.getUser().getId());
    }

    @Override
    public List<BackendDiscordAccount> getDiscordAccounts(@NotNull DatabaseDiscordAccountListAction action) throws DatabaseException {
        return this.entityDatabase.getDiscordAccounts();
    }

    @Override
    public BackendDiscordAccount getDiscordAccount(@NotNull DatabaseDiscordAccountGetAction action) throws DatabaseException {
        return this.entityDatabase.getDiscordAccount(action.getId());
    }

    @Override
    public BackendDiscordAccount createDiscordAccount(@NotNull DatabaseDiscordAccountBuilder builder) throws DatabaseException {
        final long id = this.idGenerator.newId();
        return this.entityDatabase.createDiscordAccount(id, builder.getSnowflake(), builder.getName(), ((AbstractUser) builder.getUser()));
    }

    @Override
    public void modifyDiscordAccount(@NotNull DatabaseDiscordAccountModifier modifier) throws DatabaseException {
        String oldName = modifier.getEntity().getCachedName();
        String newName = modifier.getName();
        if (!Objects.equals(oldName, newName))
            this.entityDatabase.modifyDiscordAccountName(modifier.getEntity().getId(), newName);

        User oldUser = modifier.getEntity().getUser();
        User newUser = modifier.getUser();
        if (!Objects.equals(oldUser, newUser))
            this.entityDatabase.modifyDiscordAccountUser(modifier.getEntity().getId(), newUser);
    }

    @Override
    public void deleteDiscordAccount(@NotNull DatabaseDiscordAccountDeleteAction deleteAction) throws DatabaseException {
        this.entityDatabase.deleteDiscordAccount(deleteAction.getId());
    }

    @Override
    public List<BackendMinecraftAccount> getMinecraftAccounts(@NotNull DatabaseMinecraftAccountListAction action) throws DatabaseException {
        return this.entityDatabase.getMinecraftAccounts();
    }

    @Override
    public BackendMinecraftAccount getMinecraftAccount(@NotNull DatabaseMinecraftAccountGetAction action) throws DatabaseException {
        return this.entityDatabase.getMinecraftAccount(action.getId());
    }

    @Override
    public BackendMinecraftAccount createMinecraftAccount(@NotNull DatabaseMinecraftAccountBuilder builder) throws DatabaseException {
        final long id = this.idGenerator.newId();
        return this.entityDatabase.createMinecraftAccount(id, builder.getUUID(), builder.getName(), ((AbstractUser) builder.getUser()));
    }

    @Override
    public void modifyMinecraftAccount(@NotNull DatabaseMinecraftAccountModifier modifier) throws DatabaseException {
        String oldName = modifier.getEntity().getCachedName();
        String newName = modifier.getName();
        if (!Objects.equals(oldName, newName))
            this.entityDatabase.modifyMinecraftAccountName(modifier.getEntity().getId(), newName);

        User oldUser = modifier.getEntity().getUser();
        User newUser = modifier.getUser();
        if (!Objects.equals(oldUser, newUser))
            this.entityDatabase.modifyMinecraftAccountUser(modifier.getEntity().getId(), newUser);
    }

    @Override
    public void deleteMinecraftAccount(@NotNull DatabaseMinecraftAccountDeleteAction deleteAction) throws DatabaseException {
        this.entityDatabase.deleteMinecraftAccount(deleteAction.getId());
    }
}
