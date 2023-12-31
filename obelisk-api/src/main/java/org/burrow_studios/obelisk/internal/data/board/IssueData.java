package org.burrow_studios.obelisk.internal.data.board;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.internal.data.Data;
import org.jetbrains.annotations.NotNull;

public final class IssueData extends Data<Issue> {
    public IssueData() {
        super();
    }

    public IssueData(long id) {
        super(id);
    }

    public void addAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.addToArray("assignees", arr);
    }

    public void removeAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.removeFromArray("assignees", arr);
    }

    public void setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
    }

    public void setState(@NotNull Issue.State state) {
        this.set("state", new JsonPrimitive(state.name()));
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
}
