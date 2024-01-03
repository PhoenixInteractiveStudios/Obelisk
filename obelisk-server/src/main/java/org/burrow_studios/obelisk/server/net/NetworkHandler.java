package org.burrow_studios.obelisk.server.net;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.net.http.APIHandler;
import org.burrow_studios.obelisk.server.net.http.AuthLevel;
import org.burrow_studios.obelisk.server.net.http.Method;
import org.burrow_studios.obelisk.server.net.http.handlers.*;
import org.burrow_studios.obelisk.server.net.http.server.SunServerImpl;
import org.burrow_studios.obelisk.server.net.socket.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NetworkHandler {
    private final ObeliskServer server;

    private final APIHandler      apiHandler;
    private final EventDispatcher eventDispatcher;

    public NetworkHandler(@NotNull ObeliskServer server) throws IOException {
        this.server = server;

        final SessionHandler sessionHandler = new SessionHandler(this);
        final   GroupHandler   groupHandler = new   GroupHandler(this);
        final ProjectHandler projectHandler = new ProjectHandler(this);
        final  TicketHandler  ticketHandler = new  TicketHandler(this);
        final    UserHandler    userHandler = new    UserHandler(this);
        final   BoardHandler   boardHandler = new   BoardHandler(this);
        final     TagHandler     tagHandler = new     TagHandler(this);
        final   IssueHandler   issueHandler = new   IssueHandler(this);

        this.apiHandler = new SunServerImpl(this)
                // Session lifecycle
                .addEndpoint(Method.POST  , "/session/:long"      , AuthLevel.IDENTITY, sessionHandler::onLogin)
                .addEndpoint(Method.DELETE, "/session/:long/:long", AuthLevel.SESSION , sessionHandler::onLogout)
                .addEndpoint(Method.DELETE, "/session/:long"      , AuthLevel.IDENTITY, sessionHandler::onLogoutAll)
                // Resource: Group
                .addEndpoint(Method.GET   , "/groups"                    , AuthLevel.SESSION, groupHandler::onGetAll)
                .addEndpoint(Method.GET   , "/groups/:long"              , AuthLevel.SESSION, groupHandler::onGet)
                .addEndpoint(Method.POST  , "/groups"                    , AuthLevel.SESSION, groupHandler::onCreate)
                .addEndpoint(Method.PUT   , "/groups/:long/members/:long", AuthLevel.SESSION, groupHandler::onAddMember)
                .addEndpoint(Method.DELETE, "/groups/:long/members/:long", AuthLevel.SESSION, groupHandler::onDeleteMember)
                .addEndpoint(Method.DELETE, "/groups/:long"              , AuthLevel.SESSION, groupHandler::onDelete)
                .addEndpoint(Method.PATCH , "/groups/:long"              , AuthLevel.SESSION, groupHandler::onEdit)
                // Resource: Project
                .addEndpoint(Method.GET   , "/projects"                    , AuthLevel.SESSION, projectHandler::onGetAll)
                .addEndpoint(Method.GET   , "/projects/:long"              , AuthLevel.SESSION, projectHandler::onGet)
                .addEndpoint(Method.POST  , "/projects"                    , AuthLevel.SESSION, projectHandler::onCreate)
                .addEndpoint(Method.PUT   , "/projects/:long/members/:long", AuthLevel.SESSION, projectHandler::onAddMember)
                .addEndpoint(Method.DELETE, "/projects/:long/members/:long", AuthLevel.SESSION, projectHandler::onDeleteMember)
                .addEndpoint(Method.DELETE, "/projects/:long"              , AuthLevel.SESSION, projectHandler::onDelete)
                .addEndpoint(Method.PATCH , "/projects/:long"              , AuthLevel.SESSION, projectHandler::onEdit)
                // Resource: Ticket
                .addEndpoint(Method.GET   , "/tickets"                  , AuthLevel.SESSION, ticketHandler::onGetAll)
                .addEndpoint(Method.GET   , "/tickets/:long"            , AuthLevel.SESSION, ticketHandler::onGet)
                .addEndpoint(Method.POST  , "/tickets"                  , AuthLevel.SESSION, ticketHandler::onCreate)
                .addEndpoint(Method.PUT   , "/tickets/:long/users/:long", AuthLevel.SESSION, ticketHandler::onAddUser)
                .addEndpoint(Method.DELETE, "/tickets/:long/users/:long", AuthLevel.SESSION, ticketHandler::onDeleteUser)
                .addEndpoint(Method.DELETE, "/tickets/:long"            , AuthLevel.SESSION, ticketHandler::onDelete)
                .addEndpoint(Method.PATCH , "/tickets/:long"            , AuthLevel.SESSION, ticketHandler::onEdit)
                // Resource: User
                .addEndpoint(Method.GET   , "/users"      , AuthLevel.SESSION, userHandler::onGetAll)
                .addEndpoint(Method.GET   , "/users/:long", AuthLevel.SESSION, userHandler::onGet)
                .addEndpoint(Method.POST  , "/users"      , AuthLevel.SESSION, userHandler::onCreate)
                .addEndpoint(Method.DELETE, "/users/:long", AuthLevel.SESSION, userHandler::onDelete)
                .addEndpoint(Method.PATCH , "/users/:long", AuthLevel.SESSION, userHandler::onEdit)
                // Resource: Board
                .addEndpoint(Method.GET   , "/boards"      , AuthLevel.SESSION, boardHandler::onGetAll)
                .addEndpoint(Method.GET   , "/boards/:long", AuthLevel.SESSION, boardHandler::onGet)
                .addEndpoint(Method.POST  , "/boards"      , AuthLevel.SESSION, boardHandler::onCreate)
                .addEndpoint(Method.DELETE, "/boards/:long", AuthLevel.SESSION, boardHandler::onDelete)
                .addEndpoint(Method.PATCH , "/boards/:long", AuthLevel.SESSION, boardHandler::onEdit)
                // Resource: Tag
                .addEndpoint(Method.GET   , "/boards/:long/tags"      , AuthLevel.SESSION, tagHandler::onGetAll)
                .addEndpoint(Method.GET   , "/boards/:long/tags/:long", AuthLevel.SESSION, tagHandler::onGet)
                .addEndpoint(Method.POST  , "/boards/:long/tags"      , AuthLevel.SESSION, tagHandler::onCreate)
                .addEndpoint(Method.DELETE, "/boards/:long/tags/:long", AuthLevel.SESSION, tagHandler::onDelete)
                .addEndpoint(Method.PATCH , "/boards/:long/tags/:long", AuthLevel.SESSION, tagHandler::onEdit)
                // Resource: Ticket
                .addEndpoint(Method.GET   , "/boards/:long/issues"                      , AuthLevel.SESSION, issueHandler::onGetAll)
                .addEndpoint(Method.GET   , "/boards/:long/issues/:long"                , AuthLevel.SESSION, issueHandler::onGet)
                .addEndpoint(Method.POST  , "/boards/:long/issues"                      , AuthLevel.SESSION, issueHandler::onCreate)
                .addEndpoint(Method.PUT   , "/boards/:long/issues/:long/assignees/:long", AuthLevel.SESSION, issueHandler::onAddAssignee)
                .addEndpoint(Method.DELETE, "/boards/:long/issues/:long/assignees/:long", AuthLevel.SESSION, issueHandler::onDeleteAssignee)
                .addEndpoint(Method.PUT   , "/boards/:long/issues/:long/tags/:long"     , AuthLevel.SESSION, issueHandler::onAddTag)
                .addEndpoint(Method.DELETE, "/boards/:long/issues/:long/tags/:long"     , AuthLevel.SESSION, issueHandler::onDeleteTag)
                .addEndpoint(Method.DELETE, "/boards/:long/issues/:long"                , AuthLevel.SESSION, issueHandler::onDelete)
                .addEndpoint(Method.PATCH , "/boards/:long/issues/:long"                , AuthLevel.SESSION, issueHandler::onEdit);

        this.eventDispatcher = new EventDispatcher(this);
    }

    public @NotNull ObeliskServer getServer() {
        return server;
    }

    public @NotNull APIHandler getApiHandler() {
        return apiHandler;
    }

    public @NotNull EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
}
