package org.burrow_studios.obelisk.internal.action.entity.board.tag;

import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagModifier;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.EntityUpdater;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.issue.TagImpl;
import org.burrow_studios.obelisk.internal.net.http.Route;
import org.jetbrains.annotations.NotNull;

public class TagModifierImpl extends ModifierImpl<Tag> implements TagModifier {
    public TagModifierImpl(@NotNull TagImpl tag) {
        super(
                tag,
                Route.Board.Tag.EDIT.builder()
                        .withArg(tag.getBoardId())
                        .withArg(tag.getId())
                        .compile(),
                json -> EntityUpdater.updateTag(tag, json)
        );
    }

    @Override
    public @NotNull TagModifier setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
        return this;
    }
}
