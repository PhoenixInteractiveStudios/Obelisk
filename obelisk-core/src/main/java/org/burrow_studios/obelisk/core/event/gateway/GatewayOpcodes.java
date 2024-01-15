package org.burrow_studios.obelisk.core.event.gateway;

public final class GatewayOpcodes {
    public static final int GROUP_CREATE_EVENT          = 100;
    public static final int GROUP_DELETE_EVENT          = 101;
    public static final int GROUP_UPDATE_MEMBERS_EVENT  = 102;
    public static final int GROUP_UPDATE_NAME_EVENT     = 103;
    public static final int GROUP_UPDATE_POSITION_EVENT = 104;

    public static final int PROJECT_CREATE_EVENT         = 110;
    public static final int PROJECT_DELETE_EVENT         = 111;
    public static final int PROJECT_UPDATE_MEMBERS_EVENT = 112;
    public static final int PROJECT_UPDATE_STATE_EVENT   = 113;
    public static final int PROJECT_UPDATE_TIMINGS_EVENT = 114;
    public static final int PROJECT_UPDATE_TITLE_EVENT   = 115;

    public static final int TICKET_CREATE_EVENT       = 120;
    public static final int TICKET_DELETE_EVENT       = 121;
    public static final int TICKET_UPDATE_STATE_EVENT = 123;
    public static final int TICKET_UPDATE_TAGS_EVENT  = 124;
    public static final int TICKET_UPDATE_TITLE_EVENT = 125;
    public static final int TICKET_UPDATE_USERS_EVENT = 126;

    public static final int USER_CREATE_EVENT               = 130;
    public static final int USER_DELETE_EVENT               = 131;
    public static final int USER_UPDATE_DISCORD_IDS_EVENT   = 132;
    public static final int USER_UPDATE_MINECRAFT_IDS_EVENT = 133;
    public static final int USER_UPDATE_NAME_EVENT          = 134;

    public static final int BOARD_CREATE_EVENT       = 140;
    public static final int BOARD_DELETE_EVENT       = 141;
    public static final int BOARD_UPDATE_GROUP_EVENT = 143;
    public static final int BOARD_UPDATE_TITLE_EVENT = 145;

    public static final int ISSUE_CREATE_EVENT           = 150;
    public static final int ISSUE_DELETE_EVENT           = 151;
    public static final int ISSUE_UPDATE_ASSIGNEES_EVENT = 152;
    public static final int ISSUE_UPDATE_STATE_EVENT     = 153;
    public static final int ISSUE_UPDATE_TAGS_EVENT      = 154;
    public static final int ISSUE_UPDATE_TITLE_EVENT     = 155;

    public static final int TAG_CREATE_EVENT      = 160;
    public static final int TAG_DELETE_EVENT      = 161;
    public static final int TAG_UPDATE_NAME_EVENT = 163;

    private GatewayOpcodes() { }
}
