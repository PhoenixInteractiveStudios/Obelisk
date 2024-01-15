package org.burrow_studios.obelisk.core.event.gateway;

import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateGroupEvent;
import org.burrow_studios.obelisk.api.event.entity.board.board.BoardUpdateTitleEvent;
import org.burrow_studios.obelisk.api.event.entity.board.issue.*;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.board.tag.TagUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateMembersEvent;
import org.burrow_studios.obelisk.api.event.entity.group.GroupUpdateNameEvent;
import org.burrow_studios.obelisk.api.event.entity.project.*;
import org.burrow_studios.obelisk.api.event.entity.ticket.*;
import org.burrow_studios.obelisk.api.event.entity.user.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public final class GatewayOpcodes {
    private static final GatewayOpcodes INSTANCE = new GatewayOpcodes()
            // GROUP
            .add(100, GroupCreateEvent.class)
            .add(101, GroupDeleteEvent.class)
            .add(102, GroupUpdateMembersEvent.class)
            .add(103, GroupUpdateNameEvent.class)
            .add(104, GroupUpdateNameEvent.class)
            // PROJECT
            .add(110, ProjectCreateEvent.class)
            .add(111, ProjectDeleteEvent.class)
            .add(112, ProjectUpdateMembersEvent.class)
            .add(113, ProjectUpdateStateEvent.class)
            .add(114, ProjectUpdateTimingsEvent.class)
            .add(115, ProjectUpdateTitleEvent.class)
            // TICKET
            .add(120, TicketCreateEvent.class)
            .add(121, TicketDeleteEvent.class)
            .add(122, TicketUpdateStateEvent.class)
            .add(123, TicketUpdateTagsEvent.class)
            .add(124, TicketUpdateTitleEvent.class)
            .add(125, TicketUpdateUsersEvent.class)
            // USER
            .add(130, UserCreateEvent.class)
            .add(131, UserDeleteEvent.class)
            .add(132, UserUpdateDiscordIdsEvent.class)
            .add(133, UserUpdateMinecraftIdsEvent.class)
            .add(134, UserUpdateNameEvent.class)
            // BOARD
            .add(140, BoardCreateEvent.class)
            .add(141, BoardDeleteEvent.class)
            .add(142, BoardUpdateGroupEvent.class)
            .add(143, BoardUpdateTitleEvent.class)
            // ISSUE
            .add(150, IssueCreateEvent.class)
            .add(151, IssueDeleteEvent.class)
            .add(152, IssueUpdateAssigneesEvent.class)
            .add(153, IssueUpdateStateEvent.class)
            .add(154, IssueUpdateTagsEvent.class)
            .add(155, IssueUpdateTagsEvent.class)
            // TAG
            .add(160, TagCreateEvent.class)
            .add(161, TagDeleteEvent.class)
            .add(162, TagUpdateNameEvent.class)
            ;

    public static Class<? extends GatewayEvent> get(int code) {
        return INSTANCE.opcodeToEvent.get(code);
    }

    public static int get(@NotNull Class<? extends GatewayEvent> type) {
        return INSTANCE.eventToOpcode.get(type);
    }

    /* - - - */

    private final ConcurrentHashMap<Integer, Class<? extends GatewayEvent>> opcodeToEvent = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<? extends GatewayEvent>, Integer> eventToOpcode = new ConcurrentHashMap<>();

    private GatewayOpcodes() { }

    private GatewayOpcodes add(int code, @NotNull Class<? extends GatewayEvent> type) {
        this.opcodeToEvent.put(code, type);
        this.eventToOpcode.put(type, code);
        return this;
    }
}
