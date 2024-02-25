package org.burrow_studios.obelisk.shelly.net;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.shelly.Shelly;
import org.burrow_studios.obelisk.shelly.crypto.TokenManager;
import org.burrow_studios.obelisk.shelly.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

public class SessionHandler {
    private final @NotNull Shelly shelly;

    public SessionHandler(@NotNull Shelly shelly) {
        this.shelly = shelly;
    }

    public void onLogin(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.IDENTITY;
        assert request.token() != null;

        final TokenManager tokenManager = getTokenManager();

        final String subjectStr = request.getPath().split("/")[1];
        final long   subject    = Long.parseLong(subjectStr);
        final long   identity   = Long.parseLong(request.token().getId());

        final String sessionToken = tokenManager.newSessionToken(identity, subject);

        final JsonObject responseJson = new JsonObject();
        responseJson.addProperty("session", sessionToken);

        response.setBody(responseJson);
        response.setStatus(Status.OK);
    }

    public void onLogout(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException, DatabaseException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.SESSION;
        assert request.token() != null;

        final String[] pathSegments = request.getPath().split("/");

        final long subject  = Long.parseLong(pathSegments[1]);
        final long session  = Long.parseLong(pathSegments[2]);
        final long identity = Long.parseLong(request.token().getKeyId());

        // validate subject
        final long tokenSubject = Long.parseLong(request.token().getSubject());
        if (subject != tokenSubject)
            throw new ForbiddenException("Mismatch between token subject and requested subject resource. You may only access your own sessions.");

        this.shelly.getDatabase().invalidateSession(session, identity);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onLogoutAll(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.SESSION;
        assert request.token() != null;

        final String subjectStr = request.getPath().split("/")[1];
        final long   subject    = Long.parseLong(subjectStr);
        final long   identity   = Long.parseLong(request.token().getKeyId());

        // validate subject
        final long tokenSubject = Long.parseLong(request.token().getSubject());
        if (subject != tokenSubject)
            throw new ForbiddenException("Mismatch between token subject and requested subject resource. You may only access your own sessions.");

        this.shelly.getDatabase().invalidateAllSessions(identity);

        response.setStatus(Status.NO_CONTENT);
    }

    public void onGetSocket(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        JsonObject body = new JsonObject();
        body.addProperty("host", "api.burrow-studios.org");
        body.addProperty("port", 8346);

        response.setStatus(Status.OK);
        response.setBody(body);
    }

    private @NotNull TokenManager getTokenManager() {
        return this.shelly.getTokenManager();
    }
}
