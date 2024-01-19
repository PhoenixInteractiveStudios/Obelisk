package org.burrow_studios.obelisk.server.net;

import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.net.http.APIHandler;
import org.burrow_studios.obelisk.server.net.http.handlers.*;
import org.burrow_studios.obelisk.server.net.http.server.SunServerImpl;
import org.burrow_studios.obelisk.server.net.socket.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NetworkHandler {
    private final ObeliskServer server;

    private final APIHandler      apiHandler;
    private final EventDispatcher eventDispatcher;

    public NetworkHandler(@NotNull ObeliskServer server) throws IOException, NetworkException {
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
                .addEndpoint(Endpoints.LOGIN     , sessionHandler::onLogin)
                .addEndpoint(Endpoints.LOGOUT    , sessionHandler::onLogout)
                .addEndpoint(Endpoints.LOGOUT_ALL, sessionHandler::onLogoutAll)
                .addEndpoint(Endpoints.GET_SOCKET, sessionHandler::onGetSocket)
                // Resource: Group
                .addEndpoint(Endpoints.Group.GET_ALL   , groupHandler::onGetAll)
                .addEndpoint(Endpoints.Group.GET       , groupHandler::onGet)
                .addEndpoint(Endpoints.Group.CREATE    , groupHandler::onCreate)
                .addEndpoint(Endpoints.Group.ADD_MEMBER, groupHandler::onAddMember)
                .addEndpoint(Endpoints.Group.DEL_MEMBER, groupHandler::onDeleteMember)
                .addEndpoint(Endpoints.Group.DELETE    , groupHandler::onDelete)
                .addEndpoint(Endpoints.Group.EDIT      , groupHandler::onEdit)
                // Resource: Project
                .addEndpoint(Endpoints.Project.GET_ALL   , projectHandler::onGetAll)
                .addEndpoint(Endpoints.Project.GET       , projectHandler::onGet)
                .addEndpoint(Endpoints.Project.CREATE    , projectHandler::onCreate)
                .addEndpoint(Endpoints.Project.ADD_MEMBER, projectHandler::onAddMember)
                .addEndpoint(Endpoints.Project.DEL_MEMBER, projectHandler::onDeleteMember)
                .addEndpoint(Endpoints.Project.DELETE    , projectHandler::onDelete)
                .addEndpoint(Endpoints.Project.EDIT      , projectHandler::onEdit)
                // Resource: Ticket
                .addEndpoint(Endpoints.Ticket.GET_ALL , ticketHandler::onGetAll)
                .addEndpoint(Endpoints.Ticket.GET     , ticketHandler::onGet)
                .addEndpoint(Endpoints.Ticket.CREATE  , ticketHandler::onCreate)
                .addEndpoint(Endpoints.Ticket.ADD_USER, ticketHandler::onAddUser)
                .addEndpoint(Endpoints.Ticket.DEL_USER, ticketHandler::onDeleteUser)
                .addEndpoint(Endpoints.Ticket.DELETE  , ticketHandler::onDelete)
                .addEndpoint(Endpoints.Ticket.EDIT    , ticketHandler::onEdit)
                // Resource: User
                .addEndpoint(Endpoints.User.GET_ALL, userHandler::onGetAll)
                .addEndpoint(Endpoints.User.GET    , userHandler::onGet)
                .addEndpoint(Endpoints.User.CREATE , userHandler::onCreate)
                .addEndpoint(Endpoints.User.DELETE , userHandler::onDelete)
                .addEndpoint(Endpoints.User.EDIT   , userHandler::onEdit)
                // Resource: Board
                .addEndpoint(Endpoints.Board.GET_ALL, boardHandler::onGetAll)
                .addEndpoint(Endpoints.Board.GET    , boardHandler::onGet)
                .addEndpoint(Endpoints.Board.CREATE , boardHandler::onCreate)
                .addEndpoint(Endpoints.Board.DELETE , boardHandler::onDelete)
                .addEndpoint(Endpoints.Board.EDIT   , boardHandler::onEdit)
                // Resource: Tag
                .addEndpoint(Endpoints.Board.Tag.GET_ALL, tagHandler::onGetAll)
                .addEndpoint(Endpoints.Board.Tag.GET    , tagHandler::onGet)
                .addEndpoint(Endpoints.Board.Tag.CREATE , tagHandler::onCreate)
                .addEndpoint(Endpoints.Board.Tag.DELETE , tagHandler::onDelete)
                .addEndpoint(Endpoints.Board.Tag.EDIT   , tagHandler::onEdit)
                // Resource: Issues
                .addEndpoint(Endpoints.Board.Issue.GET_ALL     , issueHandler::onGetAll)
                .addEndpoint(Endpoints.Board.Issue.GET         , issueHandler::onGet)
                .addEndpoint(Endpoints.Board.Issue.CREATE      , issueHandler::onCreate)
                .addEndpoint(Endpoints.Board.Issue.ADD_ASSIGNEE, issueHandler::onAddAssignee)
                .addEndpoint(Endpoints.Board.Issue.DEL_ASSIGNEE, issueHandler::onDeleteAssignee)
                .addEndpoint(Endpoints.Board.Issue.ADD_TAG     , issueHandler::onAddTag)
                .addEndpoint(Endpoints.Board.Issue.DEL_TAG     , issueHandler::onDeleteTag)
                .addEndpoint(Endpoints.Board.Issue.DELETE      , issueHandler::onDelete)
                .addEndpoint(Endpoints.Board.Issue.EDIT        , issueHandler::onEdit);

        this.eventDispatcher = new EventDispatcher(this, /* TODO */ 8346);
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
