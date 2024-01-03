package org.burrow_studios.obelisk.server.net.http.handlers;

import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.jetbrains.annotations.NotNull;

public class IssueHandler {
    private final @NotNull NetworkHandler networkHandler;

    public IssueHandler(@NotNull NetworkHandler networkHandler) {
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

    public void onAddAssignee(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onDeleteAssignee(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onAddTag(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onDeleteTag(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        // TODO
    }
}
