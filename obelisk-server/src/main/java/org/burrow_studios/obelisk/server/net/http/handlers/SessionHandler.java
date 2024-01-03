package org.burrow_studios.obelisk.server.net.http.handlers;

import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.jetbrains.annotations.NotNull;

public class SessionHandler {
    private final @NotNull NetworkHandler networkHandler;

    public SessionHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onLogin(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onLogout(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onLogoutAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }
}
