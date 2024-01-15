package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class TicketUpdateTagsEvent extends TicketUpdateEvent<Set<String>> {
    private final @NotNull Set<String>   addedTags;
    private final @NotNull Set<String> removedTags;

    public TicketUpdateTagsEvent(long id, @NotNull Ticket entity, @NotNull Set<String> oldValue, @NotNull Set<String> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedTags = newValue.stream()
                .filter(tag -> !oldValue.contains(tag))
                .collect(Collectors.toUnmodifiableSet());
        this.removedTags = oldValue.stream()
                .filter(tag -> !newValue.contains(tag))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<String> getAddedTags() {
        return this.addedTags;
    }

    public @NotNull Set<String> getRemovedTags() {
        return this.removedTags;
    }
}
