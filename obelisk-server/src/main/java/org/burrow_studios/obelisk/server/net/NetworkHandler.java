package org.burrow_studios.obelisk.server.net;

import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.net.http.APIHandler;
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
                .addEndpoint(Method.POST  , "/session/:long"      , sessionHandler::onLogin)
                .addEndpoint(Method.DELETE, "/session/:long/:long", sessionHandler::onLogout)
                .addEndpoint(Method.DELETE, "/session/:long"      , sessionHandler::onLogoutAll)
                // Resource: Group
                .addEndpoint(Method.GET   , "/groups"                    , groupHandler::onGetAll)
                .addEndpoint(Method.GET   , "/groups/:long"              , groupHandler::onGet)
                .addEndpoint(Method.POST  , "/groups"                    , groupHandler::onCreate)
                .addEndpoint(Method.PUT   , "/groups/:long/members/:long", groupHandler::onAddMember)
                .addEndpoint(Method.DELETE, "/groups/:long/members/:long", groupHandler::onDeleteMember)
                .addEndpoint(Method.DELETE, "/groups/:long"              , groupHandler::onDelete)
                .addEndpoint(Method.PATCH , "/groups/:long"              , groupHandler::onEdit)
                // Resource: Project
                .addEndpoint(Method.GET   , "/projects"                    , projectHandler::onGetAll)
                .addEndpoint(Method.GET   , "/projects/:long"              , projectHandler::onGet)
                .addEndpoint(Method.POST  , "/projects"                    , projectHandler::onCreate)
                .addEndpoint(Method.PUT   , "/projects/:long/members/:long", projectHandler::onAddMember)
                .addEndpoint(Method.DELETE, "/projects/:long/members/:long", projectHandler::onDeleteMember)
                .addEndpoint(Method.DELETE, "/projects/:long"              , projectHandler::onDelete)
                .addEndpoint(Method.PATCH , "/projects/:long"              , projectHandler::onEdit)
                // Resource: Ticket
                .addEndpoint(Method.GET   , "/tickets"                  , ticketHandler::onGetAll)
                .addEndpoint(Method.GET   , "/tickets/:long"            , ticketHandler::onGet)
                .addEndpoint(Method.POST  , "/tickets"                  , ticketHandler::onCreate)
                .addEndpoint(Method.PUT   , "/tickets/:long/users/:long", ticketHandler::onAddUser)
                .addEndpoint(Method.DELETE, "/tickets/:long/users/:long", ticketHandler::onDeleteUser)
                .addEndpoint(Method.DELETE, "/tickets/:long"            , ticketHandler::onDelete)
                .addEndpoint(Method.PATCH , "/tickets/:long"            , ticketHandler::onEdit)
                // Resource: User
                .addEndpoint(Method.GET   , "/users"      , userHandler::onGetAll)
                .addEndpoint(Method.GET   , "/users/:long", userHandler::onGet)
                .addEndpoint(Method.POST  , "/users"      , userHandler::onCreate)
                .addEndpoint(Method.DELETE, "/users/:long", userHandler::onDelete)
                .addEndpoint(Method.PATCH , "/users/:long", userHandler::onEdit)
                // Resource: Board
                .addEndpoint(Method.GET   , "/boards"      , boardHandler::onGetAll)
                .addEndpoint(Method.GET   , "/boards/:long", boardHandler::onGet)
                .addEndpoint(Method.POST  , "/boards"      , boardHandler::onCreate)
                .addEndpoint(Method.DELETE, "/boards/:long", boardHandler::onDelete)
                .addEndpoint(Method.PATCH , "/boards/:long", boardHandler::onEdit)
                // Resource: Tag
                .addEndpoint(Method.GET   , "/boards/:long/tags"      , tagHandler::onGetAll)
                .addEndpoint(Method.GET   , "/boards/:long/tags/:long", tagHandler::onGet)
                .addEndpoint(Method.POST  , "/boards/:long/tags"      , tagHandler::onCreate)
                .addEndpoint(Method.DELETE, "/boards/:long/tags/:long", tagHandler::onDelete)
                .addEndpoint(Method.PATCH , "/boards/:long/tags/:long", tagHandler::onEdit)
                // Resource: Ticket
                .addEndpoint(Method.GET   , "/boards/:long/issues"                      , issueHandler::onGetAll)
                .addEndpoint(Method.GET   , "/boards/:long/issues/:long"                , issueHandler::onGet)
                .addEndpoint(Method.POST  , "/boards/:long/issues"                      , issueHandler::onCreate)
                .addEndpoint(Method.PUT   , "/boards/:long/issues/:long/assignees/:long", issueHandler::onAddAssignee)
                .addEndpoint(Method.DELETE, "/boards/:long/issues/:long/assignees/:long", issueHandler::onDeleteAssignee)
                .addEndpoint(Method.PUT   , "/boards/:long/issues/:long/tags/:long"     , issueHandler::onAddTag)
                .addEndpoint(Method.DELETE, "/boards/:long/issues/:long/tags/:long"     , issueHandler::onDeleteTag)
                .addEndpoint(Method.DELETE, "/boards/:long/issues/:long"                , issueHandler::onDelete)
                .addEndpoint(Method.PATCH , "/boards/:long/issues/:long"                , issueHandler::onEdit);

        this.eventDispatcher = new EventDispatcher(this);
    }

    public @NotNull APIHandler getApiHandler() {
        return apiHandler;
    }

    public @NotNull EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }
}
