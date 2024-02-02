package org.burrow_studios.obelisk.server.net.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.http.AuthLevel;
import org.burrow_studios.obelisk.commons.http.server.Request;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.burrow_studios.obelisk.commons.http.server.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.server.auth.Authenticator;
import org.burrow_studios.obelisk.server.auth.crypto.TokenManager;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

public class SessionHandler {
    private final @NotNull NetworkHandler networkHandler;

    public SessionHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onLogin(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.IDENTITY;
        assert request.token() != null;

        final TokenManager tokenManager = getTokenManager();

        final long subject  = request.getSegmentLong(1);
        final long identity = Long.parseLong(request.token().getId());

        final String sessionToken = tokenManager.newSessionToken(identity, subject);

        final JsonObject responseJson = new JsonObject();
        responseJson.addProperty("session", sessionToken);

        response.setBody(responseJson);
        response.setCode(200);
    }

    public void onLogout(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException, DatabaseException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.SESSION;
        assert request.token() != null;

        final long subject  = request.getSegmentLong(1);
        final long session  = request.getSegmentLong(2);
        final long identity = Long.parseLong(request.token().getKeyId());

        // validate subject
        final long tokenSubject = Long.parseLong(request.token().getSubject());
        if (subject != tokenSubject)
            throw new ForbiddenException("Mismatch between token subject and requested subject resource. You may only access your own sessions.");

        getAuthenticator().getDatabase().invalidateSession(session, identity);

        response.setCode(204);
    }

    public void onLogoutAll(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.SESSION;
        assert request.token() != null;

        final long subject = request.getSegmentLong(1);
        final long identity = Long.parseLong(request.token().getKeyId());

        // validate subject
        final long tokenSubject = Long.parseLong(request.token().getSubject());
        if (subject != tokenSubject)
            throw new ForbiddenException("Mismatch between token subject and requested subject resource. You may only access your own sessions.");

        getAuthenticator().getDatabase().invalidateAllSessions(identity);

        response.setCode(204);
    }

    public void onGetSocket(@NotNull Request request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        JsonObject body = new JsonObject();
        body.addProperty("host", "api.burrow-studios.org");
        body.addProperty("port", 8346);

        response.setCode(200);
        response.setBody(body);
    }

    private @NotNull Authenticator getAuthenticator() {
        return this.networkHandler.getServer().getAuthenticator();
    }

    private @NotNull TokenManager getTokenManager() {
        return this.getAuthenticator().getTokenManager();
    }
}
