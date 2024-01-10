package org.burrow_studios.obelisk.server.net.http.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.its.IssueTracker;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.Request;
import org.burrow_studios.obelisk.server.net.http.ResponseBuilder;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.burrow_studios.obelisk.server.net.http.exceptions.BadRequestException;
import org.burrow_studios.obelisk.server.net.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.server.net.http.exceptions.NotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TagHandler {
    private final @NotNull NetworkHandler networkHandler;

    public TagHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void onGetAll(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long board = request.getSegmentLong(1);

        JsonArray json = new JsonArray();
        getIssueTracker().getTags(board).forEach(json::add);

        response.setCode(200);
        response.setBody(json);
    }

    public void onGet(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long board = request.getSegmentLong(1);
        final long tag   = request.getSegmentLong(3);

        final JsonObject result;

        try {
            result = getIssueTracker().getTag(board, tag);
        } catch (NoSuchEntryException e) {
            throw new NotFoundException();
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onCreate(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long board = request.getSegmentLong(1);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            result = getIssueTracker().createTag(board, json);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public void onDelete(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        response.setCode(501);
    }

    public void onEdit(@NotNull Request request, @NotNull ResponseBuilder response) throws APIException {
        final long board = request.getSegmentLong(1);
        final long tag   = request.getSegmentLong(3);

        Optional<JsonObject> body = request.optionalBody()
                .map(JsonElement::getAsJsonObject);

        if (body.isEmpty())
            throw new BadRequestException("No content");

        final JsonObject json = body.get();
        final JsonObject result;

        try {
            getIssueTracker().patchTag(board, tag, json);

            result = getIssueTracker().getTag(board, tag);
        } catch (DatabaseException e) {
            throw new InternalServerErrorException();
        }

        response.setCode(200);
        response.setBody(result);
    }

    public @NotNull IssueTracker getIssueTracker() {
        return this.networkHandler.getServer().getIssueTracker();
    }
}
