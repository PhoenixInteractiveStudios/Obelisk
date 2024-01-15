package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class BoardUpdateIssuesEvent extends BoardUpdateEvent<Set<Issue>> {
    private final @NotNull Set<Issue>   addedIssues;
    private final @NotNull Set<Issue> removedIssues;

    public BoardUpdateIssuesEvent(long id, @NotNull Board entity, @NotNull Set<Issue> oldValue, @NotNull Set<Issue> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedIssues = newValue.stream()
                .filter(issue -> !oldValue.contains(issue))
                .collect(Collectors.toUnmodifiableSet());
        this.removedIssues = oldValue.stream()
                .filter(issue -> !newValue.contains(issue))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<Issue> getAddedIssues() {
        return this.addedIssues;
    }

    public @NotNull Set<Issue> getRemovedIssues() {
        return this.removedIssues;
    }

    @Override
    public int getOpcode() {
        return 144;
    }
}
