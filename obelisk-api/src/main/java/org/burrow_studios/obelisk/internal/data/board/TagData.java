package org.burrow_studios.obelisk.internal.data.board;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.data.Data;
import org.burrow_studios.obelisk.internal.entities.board.TagImpl;
import org.jetbrains.annotations.NotNull;

public final class TagData extends Data<Tag, TagImpl> {
    public TagData() {
        super();
    }

    public TagData(long id) {
        super(id);
    }

    @Override
    public @NotNull TagImpl build(@NotNull EntityBuilder builder) {
        return builder.buildTag(toJson());
    }

    @Override
    public void update(@NotNull TagImpl tag) {
        final JsonObject json = toJson();

        handleUpdate(json, "name", JsonElement::getAsString, tag::setName);
    }

    public void setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
    }
}
