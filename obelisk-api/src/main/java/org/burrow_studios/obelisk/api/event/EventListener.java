package org.burrow_studios.obelisk.api.event;

import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.discord.DiscordAccountUpdateUserEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.minecraft.MinecraftAccountUpdateUserEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateStateEvent;
import org.burrow_studios.obelisk.api.event.entity.project.ProjectUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.ticket.TicketUpdateUsersEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.user.UserUpdateNameEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class EventListener {
    public void onGenericEvent(@NotNull Event event) { }

    public void onUserCreate(@NotNull UserCreateEvent event) { }
    public void onUserDelete(@NotNull UserDeleteEvent event) { }
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) { }

    public void onTicketCreate(@NotNull TicketCreateEvent event) { }
    public void onTicketDelete(@NotNull TicketDeleteEvent event) { }
    public void onTicketUpdateTitle(@NotNull TicketUpdateTitleEvent event) { }
    public void onTicketUpdateState(@NotNull TicketUpdateUsersEvent event) { }

    public void onProjectCreate(@NotNull ProjectCreateEvent event) { }
    public void onProjectDelete(@NotNull ProjectDeleteEvent event) { }
    public void onProjectUpdateTitle(@NotNull ProjectUpdateTitleEvent event) { }
    public void onProjectUpdateState(@NotNull ProjectUpdateStateEvent event) { }

    public void onDiscordAccountCreate(@NotNull DiscordAccountCreateEvent event) { }
    public void onDiscordAccountDelete(@NotNull DiscordAccountDeleteEvent event) { }
    public void onDiscordAccountUpdateName(@NotNull DiscordAccountUpdateNameEvent event) { }
    public void onDiscordAccountUpdateUser(@NotNull DiscordAccountUpdateUserEvent event) { }

    public void onMinecraftAccountCreate(@NotNull MinecraftAccountCreateEvent event) { }
    public void onMinecraftAccountDelete(@NotNull MinecraftAccountDeleteEvent event) { }
    public void onMinecraftAccountUpdateName(@NotNull MinecraftAccountUpdateNameEvent event) { }
    public void onMinecraftAccountUpdateUser(@NotNull MinecraftAccountUpdateUserEvent event) { }

    /* - - - */

    public final <T extends Event> void onEvent(@NotNull T event) throws InvocationTargetException, IllegalAccessException {
        this.onGenericEvent(event);

        final String  eventName = event.getClass().getSimpleName();
        final String methodName = "on" + eventName.substring(0, eventName.length() - "Event".length());

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (!method.getName().equals(methodName)) continue;
            if (method.getParameterCount() != 1) continue;
            if (!method.getParameters()[0].getType().isInstance(event)) continue;

            method.invoke(this, event);
        }
    }
}
