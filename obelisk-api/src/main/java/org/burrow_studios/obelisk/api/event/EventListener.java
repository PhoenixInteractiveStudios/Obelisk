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

    public void onGroupUpdateName(@NotNull GroupUpdateNameEvent event) { }
    public void onGroupUpdateMembers(@NotNull GroupUpdateMembersEvent event) { }
    public void onGroupUpdatePosition(@NotNull GroupUpdatePositionEvent event) { }

    public void onProjectUpdateTitle(@NotNull ProjectUpdateTitleEvent event) { }
    public void onProjectUpdateTimings(@NotNull ProjectUpdateTimingsEvent event) { }
    public void onProjectUpdateState(@NotNull ProjectUpdateStateEvent event) { }
    public void onProjectUpdateMembers(@NotNull ProjectUpdateMembersEvent event) { }

    public void onTicketUpdateTitle(@NotNull TicketUpdateTitleEvent event) { }
    public void onTicketUpdateState(@NotNull TicketUpdateStateEvent event) { }
    public void onTicketUpdateTags(@NotNull TicketUpdateTagsEvent event) { }
    public void onTicketUpdateUsers(@NotNull TicketUpdateUsersEvent event) { }

    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) { }
    public void onUserUpdateDiscordIds(@NotNull UserUpdateDiscordIdsEvent event) { }
    public void onUserUpdateMinecraftIds(@NotNull UserUpdateMinecraftIdsEvent event) { }

    public void onBoardUpdateTitle(@NotNull BoardUpdateTitleEvent event) { }
    public void onBoardUpdateGroup(@NotNull BoardUpdateGroupEvent event) { }
    public void onBoardUpdateAvailableTags(@NotNull BoardUpdateAvailableTagsEvent event) { }
    public void onBoardUpdateIssues(@NotNull BoardUpdateIssuesEvent event) { }

    public void onIssueUpdateAssignees(@NotNull IssueUpdateAssigneesEvent event) { }
    public void onIssueUpdateTitle(@NotNull IssueUpdateTitleEvent event) { }
    public void onIssueUpdateState(@NotNull IssueUpdateStateEvent event) { }
    public void onIssueUpdateTags(@NotNull IssueUpdateTagsEvent event) { }

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
