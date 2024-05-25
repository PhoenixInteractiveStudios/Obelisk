package org.burrow_studios.obelisk.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountUpdateUserEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountUpdateUserEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateMembersEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateStateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.*;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateNameEvent;
import org.burrow_studios.obelisk.core.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EntityUpdater {
    private EntityUpdater() { }

    public static List<UserUpdateEvent<?>> updateUser(long eventId, @NotNull AbstractUser user, @NotNull JsonObject oldData, @NotNull JsonObject newData) {
        ArrayList<UserUpdateEvent<?>> events = new ArrayList<>();

        // NAME
        Optional.of(newData)
                .map(json -> json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(newName -> {
                    String oldName = oldData.get("name").getAsString();

                    events.add(new UserUpdateNameEvent(eventId, user, oldName, newName));
                    user.setName(newName);
                });

        return Collections.unmodifiableList(events);
    }

    public static List<TicketUpdateEvent<?>> updateTicket(long eventId, @NotNull AbstractTicket ticket, @NotNull JsonObject oldData, @NotNull JsonObject newData) {
        ArrayList<TicketUpdateEvent<?>> events = new ArrayList<>();

        // TITLE
        Optional.of(newData)
                .map(json -> json.get("title"))
                .ifPresent(newElement -> {
                    JsonElement oldElement = oldData.get("title");
                    String oldTitle = oldElement.isJsonNull() ? null : oldElement.getAsString();
                    String newTitle = newElement.isJsonNull() ? null : newElement.getAsString();

                    events.add(new TicketUpdateTitleEvent(eventId, ticket, oldTitle, newTitle));
                    ticket.setTitle(newTitle);
                });

        // STATE
        Optional.of(newData)
                .map(json -> json.get("state"))
                .map(JsonElement::getAsString)
                .map(Ticket.State::valueOf)
                .ifPresent(newState -> {
                    String oldStateStr = oldData.get("state").getAsString();
                    Ticket.State oldState = Ticket.State.valueOf(oldStateStr);

                    events.add(new TicketUpdateStateEvent(eventId, ticket, oldState, newState));
                    ticket.setState(newState);
                });

        // USERS
        Optional.of(newData)
                .map(json -> json.get("users"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(newElements -> {
                    EntityBuilder entityBuilder = ((ObeliskImpl) ticket.getAPI()).getEntityBuilder();

                    Set<User> oldUsers = new HashSet<>();
                    Set<User> newUsers = new HashSet<>();

                    for (JsonElement oldElement : oldData.getAsJsonArray("users")) {
                        AbstractUser oldUser = entityBuilder.provideUser(oldElement.getAsJsonObject());
                        oldUsers.add(oldUser);
                    }

                    for (JsonElement newElement : newElements) {
                        AbstractUser newUser = entityBuilder.provideUser(newElement.getAsJsonObject());
                        newUsers.add(newUser);
                    }

                    events.add(new TicketUpdateUsersEvent(eventId, ticket, oldUsers, newUsers));

                    ticket.getUsers().clear();
                    newUsers.forEach(user -> ticket.getUsers().add((AbstractUser) user));
                });

        return Collections.unmodifiableList(events);
    }

    public static List<ProjectUpdateEvent<?>> updateProject(long eventId, @NotNull AbstractProject project, @NotNull JsonObject oldData, @NotNull JsonObject newData) {
        ArrayList<ProjectUpdateEvent<?>> events = new ArrayList<>();

        // TITLE
        Optional.of(newData)
                .map(json -> json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(newTitle -> {
                    String oldTitle = oldData.get("title").getAsString();

                    events.add(new ProjectUpdateTitleEvent(eventId, project, oldTitle, newTitle));
                    project.setTitle(newTitle);
                });

        // STATE
        Optional.of(newData)
                .map(json -> json.get("state"))
                .map(JsonElement::getAsString)
                .map(Project.State::valueOf)
                .ifPresent(newState -> {
                    String oldStateStr = oldData.get("state").getAsString();
                    Project.State oldState = Project.State.valueOf(oldStateStr);

                    events.add(new ProjectUpdateStateEvent(eventId, project, oldState, newState));
                    project.setState(newState);
                });

        // USERS
        Optional.of(newData)
                .map(json -> json.get("members"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(newElements -> {
                    EntityBuilder entityBuilder = ((ObeliskImpl) project.getAPI()).getEntityBuilder();

                    Set<User> oldMembers = new HashSet<>();
                    Set<User> newMembers = new HashSet<>();

                    for (JsonElement oldElement : oldData.getAsJsonArray("users")) {
                        AbstractUser oldMember = entityBuilder.provideUser(oldElement.getAsJsonObject());
                        oldMembers.add(oldMember);
                    }

                    for (JsonElement newElement : newElements) {
                        AbstractUser newMember = entityBuilder.provideUser(newElement.getAsJsonObject());
                        newMembers.add(newMember);
                    }

                    events.add(new ProjectUpdateMembersEvent(eventId, project, oldMembers, newMembers));

                    project.getMembers().clear();
                    newMembers.forEach(user -> project.getMembers().add((AbstractUser) user));
                });

        return Collections.unmodifiableList(events);
    }

    public static List<DiscordAccountUpdateEvent<?>> updateDiscordAccount(long eventId, @NotNull AbstractDiscordAccount discordAccount, @NotNull JsonObject oldData, @NotNull JsonObject newData) {
        ArrayList<DiscordAccountUpdateEvent<?>> events = new ArrayList<>();

        // NAME
        Optional.of(newData)
                .map(json -> json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(newName -> {
                    String oldName = oldData.get("name").getAsString();

                    events.add(new DiscordAccountUpdateNameEvent(eventId, discordAccount, oldName, newName));
                    discordAccount.setCachedName(newName);
                });

        // USER
        Optional.of(newData)
                .map(json -> json.get("user"))
                .ifPresent(newElement -> {
                    EntityBuilder entityBuilder = ((ObeliskImpl) discordAccount.getAPI()).getEntityBuilder();

                    JsonElement oldElement = oldData.get("user");
                    AbstractUser oldUser = oldElement.isJsonNull() ? null : entityBuilder.provideUser(oldElement.getAsJsonObject());
                    AbstractUser newUser = newElement.isJsonNull() ? null : entityBuilder.provideUser(newElement.getAsJsonObject());

                    events.add(new DiscordAccountUpdateUserEvent(eventId, discordAccount, oldUser, newUser));
                    discordAccount.setUser(newUser);
                });

        return Collections.unmodifiableList(events);
    }

    public static List<MinecraftAccountUpdateEvent<?>> updateMinecraftAccount(long eventId, @NotNull AbstractMinecraftAccount minecraftAccount, @NotNull JsonObject oldData, @NotNull JsonObject newData) {
        ArrayList<MinecraftAccountUpdateEvent<?>> events = new ArrayList<>();

        // NAME
        Optional.of(newData)
                .map(json -> json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(newName -> {
                    String oldName = oldData.get("name").getAsString();

                    events.add(new MinecraftAccountUpdateNameEvent(eventId, minecraftAccount, oldName, newName));
                    minecraftAccount.setCachedName(newName);
                });

        // USER
        Optional.of(newData)
                .map(json -> json.get("user"))
                .ifPresent(newElement -> {
                    EntityBuilder entityBuilder = ((ObeliskImpl) minecraftAccount.getAPI()).getEntityBuilder();

                    JsonElement oldElement = oldData.get("user");
                    AbstractUser oldUser = oldElement.isJsonNull() ? null : entityBuilder.provideUser(oldElement.getAsJsonObject());
                    AbstractUser newUser = newElement.isJsonNull() ? null : entityBuilder.provideUser(newElement.getAsJsonObject());

                    events.add(new MinecraftAccountUpdateUserEvent(eventId, minecraftAccount, oldUser, newUser));
                    minecraftAccount.setUser(newUser);
                });

        return Collections.unmodifiableList(events);
    }
}
