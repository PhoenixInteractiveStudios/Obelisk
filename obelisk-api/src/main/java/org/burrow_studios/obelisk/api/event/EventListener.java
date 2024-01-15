package org.burrow_studios.obelisk.api.event;

import org.burrow_studios.obelisk.api.event.entity.group.*;
import org.burrow_studios.obelisk.api.event.entity.board.board.*;
import org.burrow_studios.obelisk.api.event.entity.board.issue.*;
import org.burrow_studios.obelisk.api.event.entity.board.tag.*;
import org.burrow_studios.obelisk.api.event.entity.project.*;
import org.burrow_studios.obelisk.api.event.entity.ticket.*;
import org.burrow_studios.obelisk.api.event.entity.user.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class EventListener {
    public void onGenericEvent(@NotNull Event event) { }

    public void onGroupCreate(@NotNull GroupCreateEvent event) { }
    public void onGroupDelete(@NotNull GroupDeleteEvent event) { }
    public void onGroupUpdate(@NotNull GroupUpdateEvent<?> event) { }
    public void onGroupUpdateName(@NotNull GroupUpdateNameEvent event) { }
    public void onGroupUpdateMembers(@NotNull GroupUpdateMembersEvent event) { }
    public void onGroupUpdatePosition(@NotNull GroupUpdatePositionEvent event) { }

    public void onProjectCreate(@NotNull ProjectCreateEvent event) { }
    public void onProjectDelete(@NotNull ProjectDeleteEvent event) { }
    public void onProjectUpdate(@NotNull ProjectUpdateEvent<?> event) { }
    public void onProjectUpdateTitle(@NotNull ProjectUpdateTitleEvent event) { }
    public void onProjectUpdateTimings(@NotNull ProjectUpdateTimingsEvent event) { }
    public void onProjectUpdateState(@NotNull ProjectUpdateStateEvent event) { }
    public void onProjectUpdateMembers(@NotNull ProjectUpdateMembersEvent event) { }

    public void onTicketCreate(@NotNull TicketCreateEvent event) { }
    public void onTicketDelete(@NotNull TicketDeleteEvent event) { }
    public void onTicketUpdate(@NotNull TicketUpdateEvent<?> event) { }
    public void onTicketUpdateTitle(@NotNull TicketUpdateTitleEvent event) { }
    public void onTicketUpdateState(@NotNull TicketUpdateStateEvent event) { }
    public void onTicketUpdateTags(@NotNull TicketUpdateTagsEvent event) { }
    public void onTicketUpdateUsers(@NotNull TicketUpdateUsersEvent event) { }

    public void onUserCreate(@NotNull UserCreateEvent event) { }
    public void onUserDelete(@NotNull UserDeleteEvent event) { }
    public void onUserUpdate(@NotNull UserUpdateEvent<?> event) { }
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) { }
    public void onUserUpdateDiscordIds(@NotNull UserUpdateDiscordIdsEvent event) { }
    public void onUserUpdateMinecraftIds(@NotNull UserUpdateMinecraftIdsEvent event) { }

    public void onBoardCreate(@NotNull BoardCreateEvent event) { }
    public void onBoardDelete(@NotNull BoardDeleteEvent event) { }
    public void onBoardUpdate(@NotNull BoardUpdateEvent<?> event) { }
    public void onBoardUpdateTitle(@NotNull BoardUpdateTitleEvent event) { }
    public void onBoardUpdateGroup(@NotNull BoardUpdateGroupEvent event) { }

    public void onIssueCreate(@NotNull IssueCreateEvent event) { }
    public void onIssueDelete(@NotNull IssueDeleteEvent event) { }
    public void onIssueUpdate(@NotNull IssueUpdateEvent<?> event) { }
    public void onIssueUpdateAssignees(@NotNull IssueUpdateAssigneesEvent event) { }
    public void onIssueUpdateTitle(@NotNull IssueUpdateTitleEvent event) { }
    public void onIssueUpdateState(@NotNull IssueUpdateStateEvent event) { }
    public void onIssueUpdateTags(@NotNull IssueUpdateTagsEvent event) { }

    public void onTagCreate(@NotNull TagCreateEvent event) { }
    public void onTagDelete(@NotNull TagDeleteEvent event) { }
    public void onTagUpdate(@NotNull TagUpdateEvent<?> event) { }
    public void onTagUpdateName(@NotNull TagUpdateNameEvent event) { }

    /* - - - */

    public final <T extends Event> void onEvent(@NotNull T event) throws InvocationTargetException, IllegalAccessException {
        this.onGenericEvent(event);

        final String eventName = event.getClass().getSimpleName();
        final String methodName = "on" + eventName.substring(0, eventName.length() - "Event".length());

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (!method.getName().equals(methodName)
                    || method.getParameterCount() != 1
                    || !method.getParameters()[0].getType().isInstance(eventName)
            ) continue;

            method.invoke(this, event);
        }
    }
}
