package org.burrow_studios.obelisk.internal.action.entity.issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.issue.IssueModifier;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.issue.Issue;
import org.burrow_studios.obelisk.api.entities.issue.Tag;
import org.burrow_studios.obelisk.internal.action.ModifierImpl;
import org.burrow_studios.obelisk.internal.entities.issue.IssueImpl;
import org.jetbrains.annotations.NotNull;

public class IssueModifierImpl extends ModifierImpl<Issue> implements IssueModifier {
    public IssueModifierImpl(@NotNull IssueImpl entity) {
        super(entity);
    }

    @Override
    public @NotNull IssueModifierImpl addAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.add("assignees", arr);
        return this;
    }

    @Override
    public @NotNull IssueModifierImpl removeAssignees(@NotNull User... users) {
        JsonArray arr = new JsonArray();
        for (User user : users)
            arr.add(user.getId());
        this.remove("assignees", arr);
        return this;
    }

    @Override
    public @NotNull IssueModifierImpl setTitle(@NotNull String title) {
        this.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull IssueModifier setState(@NotNull Issue.State state) {
        this.set("state", new JsonPrimitive(state.name()));
        return this;
    }

    @Override
    public @NotNull IssueModifierImpl addTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.add("tags", arr);
        return this;
    }

    @Override
    public @NotNull IssueModifierImpl removeTags(@NotNull Tag... tags) {
        JsonArray arr = new JsonArray();
        for (Tag tag : tags)
            arr.add(tag.getId());
        this.remove("tags", arr);
        return this;
    }
}
