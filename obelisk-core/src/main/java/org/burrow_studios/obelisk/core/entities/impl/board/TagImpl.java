package org.burrow_studios.obelisk.core.entities.impl.board;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.DeleteActionImpl;
import org.burrow_studios.obelisk.core.entities.action.board.tag.TagModifierImpl;
import org.burrow_studios.obelisk.core.entities.impl.TurtleImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public final class TagImpl extends TurtleImpl implements Tag {
    private final @NotNull BoardImpl board;
    private @NotNull String name;

    public TagImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull BoardImpl board,
            @NotNull String name
    ) {
        super(api, id);
        this.board = board;
        this.name = name;
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("board", board.getId());
        json.addProperty("name", name);
        return json;
    }

    @Override
    public @NotNull TagModifierImpl modify() {
        return new TagModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Tag> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                Tag.class,
                this.getId(),
                Route.Board.Tag.DELETE.builder()
                        .withArg(getBoard().getId())
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @NotNull Board getBoard() {
        // TODO
        return null;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}