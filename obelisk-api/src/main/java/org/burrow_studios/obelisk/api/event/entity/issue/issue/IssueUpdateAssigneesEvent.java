package org.burrow_studios.obelisk.api.event.entity.issue.issue;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class IssueUpdateAssigneesEvent extends IssueUpdateEvent<Set<User>> {
    private final @NotNull Set<User>   addedAssignees;
    private final @NotNull Set<User> removedAssignees;

    public IssueUpdateAssigneesEvent(@NotNull Issue entity, @NotNull Set<User> oldValue, @NotNull Set<User> newValue) {
        super(entity, oldValue, newValue);
        this.addedAssignees = newValue.stream()
                .filter(assignee -> !oldValue.contains(assignee))
                .collect(Collectors.toUnmodifiableSet());
        this.removedAssignees = oldValue.stream()
                .filter(assignee -> !newValue.contains(assignee))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<User> getAddedAssignees() {
        return this.addedAssignees;
    }

    public @NotNull Set<User> getRemovedAssignees() {
        return this.removedAssignees;
    }
}
