package org.burrow_studios.obelisk.internal.action.entity.board.tag;

import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.tag.TagModifier;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.issue.TagImpl;
import org.jetbrains.annotations.NotNull;

public class TagModifierImpl extends ModifierImpl<Tag> implements TagModifier {
    public TagModifierImpl(@NotNull TagImpl entity) {
        super(entity);
    }

    @Override
    public @NotNull TagModifier setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
        return this;
    }
}
