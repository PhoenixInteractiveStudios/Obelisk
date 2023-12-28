package org.burrow_studios.obelisk.api.entities.issue;

import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Issue extends Turtle {
    long getBoardId();

    @NotNull Board getBoard();

    long getAuthorId();

    @NotNull User getAuthor();

    @NotNull Set<Long> getAssigneeIds();

    @NotNull Set<? extends User> getAssignees();

    @NotNull String getTitle();

    @NotNull State getState();

    @NotNull Set<Long> getTagIds();

    @NotNull Set<? extends Tag> getTags();

    enum State {
        OPEN,
        RESOLVED,
        FROZEN,
        ABANDONED
    }
}
