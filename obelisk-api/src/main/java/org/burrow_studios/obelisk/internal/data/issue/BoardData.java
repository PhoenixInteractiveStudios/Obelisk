package org.burrow_studios.obelisk.internal.data.issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.data.Data;
import org.jetbrains.annotations.NotNull;

public final class BoardData extends Data<Board> {
    public BoardData() {
        super();
    }

    public BoardData(long id) {
        super(id);
    }

    public void setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
    }

    public void setGroup(@NotNull Group group) {
        this.set("group", new JsonPrimitive(group.getId()));
    }

    public void addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.addToArray("tags", arr);
    }

    public void removeTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.removeFromArray("tags", arr);
    }

    public void addIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.addToArray("issues", arr);
    }

    public void removeIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.removeFromArray("issues", arr);
    }
}
