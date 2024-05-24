package org.burrow_studios.obelisk.monolith.db;

import org.burrow_studios.obelisk.core.cache.EntityCache;
import org.burrow_studios.obelisk.core.entities.AbstractUser;
import org.burrow_studios.obelisk.monolith.action.*;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.discord.DatabaseDiscordAccountModifier;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.minecraft.DatabaseMinecraftAccountModifier;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.project.DatabaseProjectModifier;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketBuilder;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketDeleteAction;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketGetAction;
import org.burrow_studios.obelisk.monolith.action.entity.ticket.DatabaseTicketModifier;
import org.burrow_studios.obelisk.monolith.action.entity.user.*;
import org.burrow_studios.obelisk.monolith.entities.*;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseAdapter {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Set<IActionableDatabase> listeners = ConcurrentHashMap.newKeySet();

    public <T> @NotNull CompletableFuture<T> submit(@NotNull DatabaseAction<T> action) {
        CompletableFuture<T> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                this.execute(action, future);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });

        return future;
    }

    @SuppressWarnings("unchecked")
    private <T> void execute(@NotNull DatabaseAction<T> action, @NotNull CompletableFuture<T> future) throws DatabaseException {
        if (action instanceof DatabaseListAction<?> listAction) {
            if (listAction instanceof DatabaseUserListAction userListAction) {
                List<BackendUser> users = null;
                for (IActionableDatabase listener : this.listeners) {
                    users = listener.onUserList(userListAction);
                    if (users != null) break;
                }

                if (users == null) {
                    future.complete(null);
                    return;
                }

                userListAction.complete((CompletableFuture<EntityCache<AbstractUser>>) future, users);
                return;
            }

            // TODO
        } else if (action instanceof DatabaseGetAction<?> getAction) {
            if (getAction instanceof DatabaseUserGetAction userGetAction) {
                BackendUser user = null;
                for (IActionableDatabase listener : this.listeners) {
                    user = listener.onUserGet(userGetAction);
                    if (user != null) break;
                }
                future.complete((T) user);
                return;
            }

            if (getAction instanceof DatabaseTicketGetAction ticketGetAction) {
                BackendTicket ticket = null;
                for (IActionableDatabase listener : this.listeners) {
                    ticket = listener.onTicketGet(ticketGetAction);
                    if (ticket != null) break;
                }
                future.complete((T) ticket);
                return;
            }

            if (getAction instanceof DatabaseProjectGetAction projectGetAction) {
                BackendProject project = null;
                for (IActionableDatabase listener : this.listeners) {
                    project = listener.onProjectGet(projectGetAction);
                    if (project != null) break;
                }
                future.complete((T) project);
                return;
            }

            if (getAction instanceof DatabaseDiscordAccountGetAction discordAccountGetAction) {
                BackendDiscordAccount discordAccount = null;
                for (IActionableDatabase listener : this.listeners) {
                    discordAccount = listener.onDiscordAccountGet(discordAccountGetAction);
                    if (discordAccount != null) break;
                }
                future.complete((T) discordAccount);
                return;
            }

            if (getAction instanceof DatabaseMinecraftAccountGetAction minecraftAccountGetAction) {
                BackendMinecraftAccount minecraftAccount = null;
                for (IActionableDatabase listener : this.listeners) {
                    minecraftAccount = listener.onMinecraftAccountGet(minecraftAccountGetAction);
                    if (minecraftAccount != null) break;
                }
                future.complete((T) minecraftAccount);
                return;
            }
        } else if (action instanceof DatabaseBuilder<?> createAction) {
            if (createAction instanceof DatabaseUserBuilder userBuilder) {
                BackendUser user = null;
                for (IActionableDatabase listener : this.listeners) {
                    user = listener.onUserBuild(userBuilder);
                    if (user != null) break;
                }
                future.complete((T) user);
                return;
            }

            if (createAction instanceof DatabaseTicketBuilder ticketBuilder) {
                BackendTicket ticket = null;
                for (IActionableDatabase listener : this.listeners) {
                    ticket = listener.onTicketBuild(ticketBuilder);
                    if (ticket != null) break;
                }
                future.complete((T) ticket);
                return;
            }

            if (createAction instanceof DatabaseProjectBuilder projectBuilder) {
                BackendProject project = null;
                for (IActionableDatabase listener : this.listeners) {
                    project = listener.onProjectBuild(projectBuilder);
                    if (project != null) break;
                }
                future.complete((T) project);
                return;
            }

            if (createAction instanceof DatabaseDiscordAccountBuilder discordAccountBuilder) {
                BackendDiscordAccount discordAccount = null;
                for (IActionableDatabase listener : this.listeners) {
                    discordAccount = listener.onDiscordAccountBuild(discordAccountBuilder);
                    if (discordAccount != null) break;
                }
                future.complete((T) discordAccount);
                return;
            }

            if (createAction instanceof DatabaseMinecraftAccountBuilder minecraftAccountBuilder) {
                BackendMinecraftAccount minecraftAccount = null;
                for (IActionableDatabase listener : this.listeners) {
                    minecraftAccount = listener.onMinecraftAccountBuild(minecraftAccountBuilder);
                    if (minecraftAccount != null) break;
                }
                future.complete((T) minecraftAccount);
                return;
            }
        } else if (action instanceof DatabaseModifier<?> editAction) {
            if (editAction instanceof DatabaseUserModifier userModifier) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onUserModify(userModifier);
                future.complete(null);
                return;
            }

            if (editAction instanceof DatabaseTicketModifier ticketModifier) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onTicketModify(ticketModifier);
                future.complete(null);
                return;
            }

            if (editAction instanceof DatabaseProjectModifier projectModifier) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onProjectModify(projectModifier);
                future.complete(null);
                return;
            }

            if (editAction instanceof DatabaseDiscordAccountModifier discordAccountModifier) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onDiscordAccountModify(discordAccountModifier);
                future.complete(null);
                return;
            }

            if (editAction instanceof DatabaseMinecraftAccountModifier minecraftAccountModifier) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onMinecraftAccountModify(minecraftAccountModifier);
                future.complete(null);
                return;
            }
        } else if (action instanceof DatabaseDeleteAction<?> deleteAction) {
            if (deleteAction instanceof DatabaseUserDeleteAction userDeleteAction) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onUserDelete(userDeleteAction);
                future.complete(null);
                return;
            }

            if (deleteAction instanceof DatabaseTicketDeleteAction ticketDeleteAction) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onTicketDelete(ticketDeleteAction);
                future.complete(null);
                return;
            }

            if (deleteAction instanceof DatabaseProjectDeleteAction projectDeleteAction) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onProjectDelete(projectDeleteAction);
                future.complete(null);
                return;
            }

            if (deleteAction instanceof DatabaseDiscordAccountDeleteAction discordAccountDeleteAction) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onDiscordAccountDelete(discordAccountDeleteAction);
                future.complete(null);
                return;
            }

            if (deleteAction instanceof DatabaseMinecraftAccountDeleteAction minecraftAccountDeleteAction) {
                for (IActionableDatabase listener : this.listeners)
                    listener.onMinecraftAccountDelete(minecraftAccountDeleteAction);
                future.complete(null);
                return;
            }
        }

        throw new Error("Not implemented");
    }

    public void registerDatabase(@NotNull IActionableDatabase database) {
        this.listeners.add(database);
    }
}
