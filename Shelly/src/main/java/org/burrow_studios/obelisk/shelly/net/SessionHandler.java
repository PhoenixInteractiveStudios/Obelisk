package org.burrow_studios.obelisk.shelly.net;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.http.AuthLevel;
import org.burrow_studios.obelisk.commons.http.server.HTTPRequest;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.burrow_studios.obelisk.commons.http.server.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.shelly.Shelly;
import org.burrow_studios.obelisk.shelly.crypto.TokenManager;
import org.burrow_studios.obelisk.shelly.database.DatabaseException;
import org.jetbrains.annotations.NotNull;

public class SessionHandler {
    private final @NotNull Shelly shelly;

    public SessionHandler(@NotNull Shelly shelly) {
        this.shelly = shelly;
    }

    public void onLogin(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
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

    public void onLogout(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException, DatabaseException {
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

        this.shelly.getDatabase().invalidateSession(session, identity);

        response.setCode(204);
    }

    public void onLogoutAll(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        // this method should not be invoked without checking authorization first
        assert request.endpoint().getPrivilege() == AuthLevel.SESSION;
        assert request.token() != null;

        final long subject = request.getSegmentLong(1);
        final long identity = Long.parseLong(request.token().getKeyId());

        // validate subject
        final long tokenSubject = Long.parseLong(request.token().getSubject());
        if (subject != tokenSubject)
            throw new ForbiddenException("Mismatch between token subject and requested subject resource. You may only access your own sessions.");

        this.shelly.getDatabase().invalidateAllSessions(identity);

        response.setCode(204);
    }

    public void onGetSocket(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        JsonObject body = new JsonObject();
        body.addProperty("host", "api.burrow-studios.org");
        body.addProperty("port", 8346);

        response.setCode(200);
        response.setBody(body);
    }

    private @NotNull TokenManager getTokenManager() {
        return this.shelly.getTokenManager();
    }
}
