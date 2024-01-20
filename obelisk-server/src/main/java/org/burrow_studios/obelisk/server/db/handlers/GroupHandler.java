package org.burrow_studios.obelisk.server.db.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.GroupImpl;
import org.burrow_studios.obelisk.core.entities.impl.UserImpl;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.burrow_studios.obelisk.server.db.RequestHandler;
import org.burrow_studios.obelisk.server.db.entity.GroupDB;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class GroupHandler implements RequestHandler {
    private final TurtleGenerator turtleGenerator;
    private final EntityProvider provider;

    public GroupHandler(@NotNull EntityProvider provider) {
        this.provider = provider;
        this.turtleGenerator = TurtleGenerator.get(provider.getClass().getSimpleName());
    }

    public @NotNull Response onGetAll(@NotNull Request request) {
        final JsonArray groups = new JsonArray();
        final Set<Long> groupIds;

        try {
            groupIds = this.getDB().getGroupIds();
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        for (long groupId : groupIds)
            groups.add(groupId);

        return request.respond(200, groups);
    }

    public @NotNull Response onGet(@NotNull Request request) {
        final long groupId = (long) request.getEndpoint().args()[1];
        final JsonObject group;

        try {
            group = this.getDB().getGroup(groupId);
        } catch (NoSuchEntryException e) {
            return request.respond(404, null);
        } catch (DatabaseException e) {
            return request.respond(500, null);
        }

        return request.respond(200, group);
    }

    public @NotNull Response onCreate(@NotNull Request request) {
        final long id = this.turtleGenerator.newId();

        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        json.addProperty("id", id);

        final GroupImpl group = new GroupImpl(provider.getAPI(), new EntityData(json));

        this.getDB().createGroup(
                group.getId(),
                group.getName(),
                group.getPosition()
        );

        for (UserImpl member : group.getMembers())
            this.getDB().addGroupMember(group.getId(), member.getId());

        return request.respond(200, group.toJson());
    }

    public @NotNull Response onAddMember(@NotNull Request request) {
        final long groupId = (long) request.getEndpoint().args()[1];
        final long  userId = (long) request.getEndpoint().args()[3];

        this.getDB().addGroupMember(groupId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDeleteMember(@NotNull Request request) {
        final long groupId = (long) request.getEndpoint().args()[1];
        final long  userId = (long) request.getEndpoint().args()[3];

        this.getDB().removeGroupMember(groupId, userId);

        return request.respond(204, null);
    }

    public @NotNull Response onDelete(@NotNull Request request) {
        final long groupId = (long) request.getEndpoint().args()[1];

        this.getDB().deleteGroup(groupId);

        return request.respond(204, null);
    }

    public @NotNull Response onEdit(@NotNull Request request) {
        final long groupId = (long) request.getEndpoint().args()[1];
        final JsonObject json = request.optionalContent()
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

        if (json == null)
            return request.respond(400, null);

        Optional.ofNullable(json.get("name"))
                .map(JsonElement::getAsString)
                .ifPresent(name -> getDB().updateGroupName(groupId, name));

        Optional.ofNullable(json.get("position"))
                .map(JsonElement::getAsInt)
                .ifPresent(pos -> getDB().updateGroupPosition(groupId, pos));

        final JsonObject group = getDB().getGroup(groupId);
        return request.respond(200, group);
    }

    private @NotNull GroupDB getDB() {
        return this.provider.getGroupDB();
    }
}
