package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.auth.Authenticator;
import org.burrow_studios.obelisk.server.auth.crypto.TokenManager;
import org.burrow_studios.obelisk.server.auth.db.DatabaseException;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.AuthLevel;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.ForbiddenException;
import org.jetbrains.annotations.NotNull;

public class SessionHandler {
    private final @NotNull NetworkHandler networkHandler;

    public SessionHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onLogin(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
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

    public void onLogout(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException, DatabaseException {
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

    public void onLogoutAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
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

    private @NotNull Authenticator getAuthenticator() {
        return this.networkHandler.getServer().getAuthenticator();
    }

    private @NotNull TokenManager getTokenManager() {
        return this.getAuthenticator().getTokenManager();
    }
}
