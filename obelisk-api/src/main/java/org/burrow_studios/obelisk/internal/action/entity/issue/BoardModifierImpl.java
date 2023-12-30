package org.burrow_studios.obelisk.internal.action.entity.issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.issue.BoardModifier;
import org.burrow_studios.obelisk.api.entities.Group;
import org.burrow_studios.obelisk.api.entities.issue.Board;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.issue.BoardImpl;
import org.jetbrains.annotations.NotNull;

public class BoardModifierImpl extends ModifierImpl<Board> implements BoardModifier {
    public BoardModifierImpl(@NotNull BoardImpl entity) {
        super(entity);
    }

    @Override
    public @NotNull BoardModifierImpl setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull BoardModifier setGroup(@NotNull Group group) {
        this.set("group", new JsonPrimitive(group.getId()));
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.add("tags", arr);
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl removeTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.remove("tags", arr);
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl addIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.add("issues", arr);
        return this;
    }

    @Override
    public @NotNull BoardModifierImpl removeIssues(@NotNull Issue... issues) {
        JsonArray arr = new JsonArray();
        for (Issue issue : issues)
            arr.add(issue.getId());
        this.remove("issues", arr);
        return this;
    }
}
