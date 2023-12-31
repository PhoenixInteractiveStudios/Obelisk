package org.burrow_studios.obelisk.internal.data.issue;

import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.data.Data;
import org.jetbrains.annotations.NotNull;

public final class TagData extends Data<Tag> {
    public TagData() {
        super();
    }

    public TagData(long id) {
        super(id);
    }

    public void setName(@NotNull String name) {
        this.set("name", new JsonPrimitive(name));
    }
}
