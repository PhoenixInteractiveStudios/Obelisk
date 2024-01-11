package org.burrow_studios.obelisk.core.entities.action.board.issue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.api.action.entity.board.issue.IssueModifier;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.core.action.ModifierImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.impl.board.IssueImpl;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

import static org.burrow_studios.obelisk.core.entities.UpdateHelper.*;

public class IssueModifierImpl extends ModifierImpl<Issue, IssueImpl> implements IssueModifier {
    public IssueModifierImpl(@NotNull IssueImpl issue) {
        super(
                issue,
                Route.Board.Issue.EDIT.builder()
                        .withArg(issue.getBoard().getId())
                        .withArg(issue.getId())
                        .compile(),
                IssueModifierImpl::update
        );
    }

    protected static void update(@NotNull EntityData data, @NotNull IssueImpl issue) {
        final JsonObject json = data.toJson();

        handleUpdateTurtles(json, "assignees", issue::getAssignees);
        handleUpdate(json, "title", JsonElement::getAsString, issue::setTitle);
        handleUpdateEnum(json, "state", Issue.State.class, issue::setState);
        handleUpdateTurtles(json, "tags", issue::getTags);
    }

    @Override
    public @NotNull IssueModifierImpl setTitle(@NotNull String title) {
        data.set("title", new JsonPrimitive(title));
        return this;
    }

    @Override
    public @NotNull IssueModifier setState(@NotNull Issue.State state) {
        data.set("state", new JsonPrimitive(state.name()));
        return this;
    }
}
