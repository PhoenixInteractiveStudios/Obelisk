package org.burrow_studios.obelisk.server.net.http.handlers;

import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.jetbrains.annotations.NotNull;

public class ProjectHandler {
    private final @NotNull NetworkHandler networkHandler;

    public ProjectHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onAddMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onDeleteMember(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }
}
