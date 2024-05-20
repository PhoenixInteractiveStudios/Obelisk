package org.burrow_studios.obelisk.monolith.db.impl;

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
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

public class ActionableDatabase implements IActionableDatabase {
    @Override
    public BackendUser onUserGet(@NotNull DatabaseUserGetAction action) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public BackendUser onUserBuild(@NotNull DatabaseUserBuilder builder) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public void onUserModify(@NotNull DatabaseUserModifier modifier) throws DatabaseException {
        // TODO
    }

    @Override
    public void onUserDelete(@NotNull DatabaseUserDeleteAction deleteAction) throws DatabaseException {
        // TODO
    }

    @Override
    public BackendTicket onTicketGet(@NotNull DatabaseTicketGetAction action) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public BackendTicket onTicketBuild(@NotNull DatabaseTicketBuilder builder) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public void onTicketModify(@NotNull DatabaseTicketModifier modifier) throws DatabaseException {
        // TODO
    }

    @Override
    public void onTicketDelete(@NotNull DatabaseTicketDeleteAction deleteAction) throws DatabaseException {
        // TODO
    }

    @Override
    public void onTicketUserAdd(@NotNull DatabaseTicketUserAddAction action) throws DatabaseException {
        // TODO
    }

    @Override
    public void onTicketUserRemove(@NotNull DatabaseTicketUserRemoveAction action) throws DatabaseException {
        // TODO
    }

    @Override
    public BackendProject onProjectGet(@NotNull DatabaseProjectGetAction action) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public BackendProject onProjectBuild(@NotNull DatabaseProjectBuilder builder) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public void onProjectModify(@NotNull DatabaseProjectModifier modifier) throws DatabaseException {
        // TODO
    }

    @Override
    public void onProjectDelete(@NotNull DatabaseProjectDeleteAction deleteAction) throws DatabaseException {
        // TODO
    }

    @Override
    public void onProjectUserAdd(@NotNull DatabaseProjectUserAddAction action) throws DatabaseException {
        // TODO
    }

    @Override
    public void onProjectUserRemove(@NotNull DatabaseProjectUserRemoveAction action) throws DatabaseException {
        // TODO
    }

    @Override
    public BackendDiscordAccount onDiscordAccountGet(@NotNull DatabaseDiscordAccountGetAction action) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public BackendDiscordAccount onDiscordAccountBuild(@NotNull DatabaseDiscordAccountBuilder builder) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public void onDiscordAccountModify(@NotNull DatabaseDiscordAccountModifier modifier) throws DatabaseException {
        // TODO
    }

    @Override
    public void onDiscordAccountDelete(@NotNull DatabaseDiscordAccountDeleteAction deleteAction) throws DatabaseException {
        // TODO
    }

    @Override
    public BackendMinecraftAccount onMinecraftAccountGet(@NotNull DatabaseMinecraftAccountGetAction action) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public BackendMinecraftAccount onMinecraftAccountBuild(@NotNull DatabaseMinecraftAccountBuilder builder) throws DatabaseException {
        // TODO
        return null;
    }

    @Override
    public void onMinecraftAccountModify(@NotNull DatabaseMinecraftAccountModifier modifier) throws DatabaseException {
        // TODO
    }

    @Override
    public void onMinecraftAccountDelete(@NotNull DatabaseMinecraftAccountDeleteAction deleteAction) throws DatabaseException {
        // TODO
    }
}
