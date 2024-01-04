package org.burrow_studios.obelisk.api.event.entity.issue.issue;

import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class IssueUpdateTagsEvent extends IssueUpdateEvent<Set<Tag>> {
    private final @NotNull Set<Tag>   addedTags;
    private final @NotNull Set<Tag> removedTags;

    public IssueUpdateTagsEvent(@NotNull Issue entity, @NotNull Set<Tag> oldValue, @NotNull Set<Tag> newValue) {
        super(entity, oldValue, newValue);
        this.addedTags = newValue.stream()
                .filter(tag -> !oldValue.contains(tag))
                .collect(Collectors.toUnmodifiableSet());
        this.removedTags = oldValue.stream()
                .filter(tag -> !newValue.contains(tag))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<Tag> getAddedTags() {
        return this.addedTags;
    }

    public @NotNull Set<Tag> getRemovedTags() {
        return this.removedTags;
    }
}
