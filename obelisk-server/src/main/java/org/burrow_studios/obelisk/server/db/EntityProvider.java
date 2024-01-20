package org.burrow_studios.obelisk.server.db;

import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.net.TimeoutContext;
import org.burrow_studios.obelisk.core.net.http.CompiledEndpoint;
import org.burrow_studios.obelisk.core.net.http.Endpoint;
import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.db.entity.*;
import org.burrow_studios.obelisk.server.db.handlers.*;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class EntityProvider implements DataProvider {
    private final ObeliskServer server;
    private final EntityDatabase database;

    private final TurtleGenerator turtleGenerator = TurtleGenerator.get(EntityProvider.class.getSimpleName());

    private final   GroupHandler   groupHandler;
    private final ProjectHandler projectHandler;
    private final  TicketHandler  ticketHandler;
    private final    UserHandler    userHandler;
    private final   BoardHandler   boardHandler;
    private final     TagHandler     tagHandler;
    private final   IssueHandler   issueHandler;

    public EntityProvider(@NotNull ObeliskServer server) {
        // TODO
        final String host     = "null";
        final int    port     = 3306;
        final String database = "null";
        final String user     = "null";
        final String pass     = "null";

        this.server = server;
        this.database = new EntityDatabase(host, port, database, user, pass);

        this.groupHandler   = new   GroupHandler(this);
        this.projectHandler = new ProjectHandler(this);
        this.ticketHandler  = new  TicketHandler(this);
        this.userHandler    = new    UserHandler(this);
        this.boardHandler   = new   BoardHandler(this);
        this.issueHandler   = new   IssueHandler(this);
        this.tagHandler     = new     TagHandler(this);
    }

    public @NotNull GroupDB getGroupDB() {
        return this.database;
    }

    public @NotNull ProjectDB getProjectDB() {
        return this.database;
    }

    public @NotNull TicketDB getTicketDB() {
        return this.database;
    }

    public @NotNull UserDB getUserDB() {
        return this.database;
    }

    public @NotNull BoardDB getBoardDB() {
        return this.database;
    }

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.server.getAPI();
    }

    @Override
    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        final CompiledEndpoint compiledEndpoint = action.getEndpoint();
        final         Endpoint         endpoint = compiledEndpoint.endpoint();

        // GROUP
        if (endpoint.equals(Endpoints.Group.GET_ALL))
            return this.submit(compiledEndpoint, groupHandler::onGetAll);
        if (endpoint.equals(Endpoints.Group.GET))
            return this.submit(compiledEndpoint, groupHandler::onGet);
        if (endpoint.equals(Endpoints.Group.CREATE))
            return this.submit(compiledEndpoint, groupHandler::onCreate);
        if (endpoint.equals(Endpoints.Group.ADD_MEMBER))
            return this.submit(compiledEndpoint, groupHandler::onAddMember);
        if (endpoint.equals(Endpoints.Group.DEL_MEMBER))
            return this.submit(compiledEndpoint, groupHandler::onDeleteMember);
        if (endpoint.equals(Endpoints.Group.DELETE))
            return this.submit(compiledEndpoint, groupHandler::onDelete);
        if (endpoint.equals(Endpoints.Group.EDIT))
            return this.submit(compiledEndpoint, groupHandler::onEdit);

        // PROJECT
        if (endpoint.equals(Endpoints.Project.GET_ALL))
            return this.submit(compiledEndpoint, projectHandler::onGetAll);
        if (endpoint.equals(Endpoints.Project.GET))
            return this.submit(compiledEndpoint, projectHandler::onGet);
        if (endpoint.equals(Endpoints.Project.CREATE))
            return this.submit(compiledEndpoint, projectHandler::onCreate);
        if (endpoint.equals(Endpoints.Project.ADD_MEMBER))
            return this.submit(compiledEndpoint, projectHandler::onAddMember);
        if (endpoint.equals(Endpoints.Project.DEL_MEMBER))
            return this.submit(compiledEndpoint, projectHandler::onDeleteMember);
        if (endpoint.equals(Endpoints.Project.DELETE))
            return this.submit(compiledEndpoint, projectHandler::onDelete);
        if (endpoint.equals(Endpoints.Project.EDIT))
            return this.submit(compiledEndpoint, projectHandler::onEdit);

        // TICKET
        if (endpoint.equals(Endpoints.Ticket.GET_ALL))
            return this.submit(compiledEndpoint, ticketHandler::onGetAll);
        if (endpoint.equals(Endpoints.Ticket.GET))
            return this.submit(compiledEndpoint, ticketHandler::onGet);
        if (endpoint.equals(Endpoints.Ticket.CREATE))
            return this.submit(compiledEndpoint, ticketHandler::onCreate);
        if (endpoint.equals(Endpoints.Ticket.ADD_USER))
            return this.submit(compiledEndpoint, ticketHandler::onAddUser);
        if (endpoint.equals(Endpoints.Ticket.DEL_USER))
            return this.submit(compiledEndpoint, ticketHandler::onDeleteUser);
        if (endpoint.equals(Endpoints.Ticket.DELETE))
            return this.submit(compiledEndpoint, ticketHandler::onDelete);
        if (endpoint.equals(Endpoints.Ticket.EDIT))
            return this.submit(compiledEndpoint, ticketHandler::onEdit);

        // USER
        if (endpoint.equals(Endpoints.User.GET_ALL))
            return this.submit(compiledEndpoint, userHandler::onGetAll);
        if (endpoint.equals(Endpoints.User.GET))
            return this.submit(compiledEndpoint, userHandler::onGet);
        if (endpoint.equals(Endpoints.User.CREATE))
            return this.submit(compiledEndpoint, userHandler::onCreate);
        if (endpoint.equals(Endpoints.User.DELETE))
            return this.submit(compiledEndpoint, userHandler::onDelete);
        if (endpoint.equals(Endpoints.User.EDIT))
            return this.submit(compiledEndpoint, userHandler::onEdit);

        // BOARD
        if (endpoint.equals(Endpoints.Board.GET_ALL))
            return this.submit(compiledEndpoint, boardHandler::onGetAll);
        if (endpoint.equals(Endpoints.Board.GET))
            return this.submit(compiledEndpoint, boardHandler::onGet);
        if (endpoint.equals(Endpoints.Board.CREATE))
            return this.submit(compiledEndpoint, boardHandler::onCreate);
        if (endpoint.equals(Endpoints.Board.DELETE))
            return this.submit(compiledEndpoint, boardHandler::onDelete);
        if (endpoint.equals(Endpoints.Board.EDIT))
            return this.submit(compiledEndpoint, boardHandler::onEdit);

        // ISSUE
        if (endpoint.equals(Endpoints.Board.Issue.GET_ALL))
            return this.submit(compiledEndpoint, issueHandler::onGetAll);
        if (endpoint.equals(Endpoints.Board.Issue.GET))
            return this.submit(compiledEndpoint, issueHandler::onGet);
        if (endpoint.equals(Endpoints.Board.Issue.CREATE))
            return this.submit(compiledEndpoint, issueHandler::onCreate);
        if (endpoint.equals(Endpoints.Board.Issue.ADD_ASSIGNEE))
            return this.submit(compiledEndpoint, issueHandler::onAddAssignee);
        if (endpoint.equals(Endpoints.Board.Issue.DEL_ASSIGNEE))
            return this.submit(compiledEndpoint, issueHandler::onDeleteAssignee);
        if (endpoint.equals(Endpoints.Board.Issue.ADD_TAG))
            return this.submit(compiledEndpoint, issueHandler::onAddTag);
        if (endpoint.equals(Endpoints.Board.Issue.DEL_TAG))
            return this.submit(compiledEndpoint, issueHandler::onDeleteTag);
        if (endpoint.equals(Endpoints.Board.Issue.DELETE))
            return this.submit(compiledEndpoint, issueHandler::onDelete);
        if (endpoint.equals(Endpoints.Board.Issue.EDIT))
            return this.submit(compiledEndpoint, issueHandler::onEdit);

        // TAG
        if (endpoint.equals(Endpoints.Board.Tag.GET_ALL))
            return this.submit(compiledEndpoint, tagHandler::onGetAll);
        if (endpoint.equals(Endpoints.Board.Tag.GET))
            return this.submit(compiledEndpoint, tagHandler::onGet);
        if (endpoint.equals(Endpoints.Board.Tag.CREATE))
            return this.submit(compiledEndpoint, tagHandler::onCreate);
        if (endpoint.equals(Endpoints.Board.Tag.DELETE))
            return this.submit(compiledEndpoint, tagHandler::onDelete);
        if (endpoint.equals(Endpoints.Board.Tag.EDIT))
            return this.submit(compiledEndpoint, tagHandler::onEdit);

        throw new RuntimeException("Not implemented");
    }

    private @NotNull Request submit(@NotNull CompiledEndpoint endpoint, @NotNull Function<Request, Response> source) {
        final long id = turtleGenerator.newId();

        Request request = new Request(this, id, endpoint, null, TimeoutContext.DEFAULT);

        final Runnable task = () -> {
            final CompletableFuture<Response> future = request.getFuture();

            try {
                future.complete(source.apply(request));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        };

        this.queue(task);

        return request;
    }

    private void queue(@NotNull Runnable task) {
        // TODO
    }
}
