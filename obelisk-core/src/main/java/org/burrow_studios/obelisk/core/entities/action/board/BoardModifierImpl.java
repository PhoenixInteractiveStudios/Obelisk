package org.burrow_studios.obelisk.core.entities.action.board;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.BoardModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.board.BoardImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public class BoardModifierImpl extends ModifierImpl<Board, BoardImpl> implements BoardModifier {
    public BoardModifierImpl(@NotNull BoardImpl board) {
        super(
                board,
                Route.Board.EDIT.builder()
                        .withArg(board.getId())
                        .compile(),
                BoardModifierImpl::update
        );
    }

    protected static void update(@NotNull EntityData data, @NotNull BoardImpl board) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "title", JsonElement::getAsString, board::setTitle);
        handleUpdateTurtle(json, "group", board.getAPI(), ObeliskImpl::getGroup, board::setGroup);
        handleUpdateTurtles(json, "tags", board::getAvailableTags);
        handleUpdateTurtles(json, "issues", board::getIssues);
    }

    @Override
    public @NotNull BoardModifierImpl setTitle(@NotNull String title) {
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull BoardModifier setGroup(@NotNull Group group) {
        data.set("group", new JsonPrimitive(group.getId()));
        return this;
    }
}
