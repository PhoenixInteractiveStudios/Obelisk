package org.burrow_studios.obelisk.core.entities.action.board.tag;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagModifier;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.checks.board.TagChecks;
import org.burrow_studios.obelisk.core.entities.impl.board.TagImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.handleUpdate;

public class TagModifierImpl extends ModifierImpl<Tag, TagImpl> implements TagModifier {
    public TagModifierImpl(@NotNull TagImpl tag) {
        super(
                tag,
                Route.Board.Tag.EDIT.builder()
                        .withArg(tag.getBoard().getId())
                        .withArg(tag.getId())
                        .compile(),
                TagModifierImpl::update
        );
    }

    protected static void update(@NotNull EntityData data, @NotNull TagImpl tag) {
        final JsonObject json = data.toJson();

        handleUpdate(json, "name", JsonElement::getAsString, tag::setName);
    }

    @Override
    public @NotNull TagModifier setName(@NotNull String name) throws IllegalArgumentException {
        TagChecks.checkName(name);
        data.set("name", new JsonPrimitive(name));
        return this;
    }
}
