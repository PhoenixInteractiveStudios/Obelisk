package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TicketUpdateTagsEvent extends TicketUpdateEvent<List<String>> {
    private final @NotNull List<String>   addedTags;
    private final @NotNull List<String> removedTags;

    public TicketUpdateTagsEvent(long id, @NotNull Ticket entity, @NotNull List<String> oldValue, @NotNull List<String> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedTags = newValue.stream()
                .filter(tag -> !oldValue.contains(tag))
                .toList();
        this.removedTags = oldValue.stream()
                .filter(tag -> !newValue.contains(tag))
                .toList();
    }

    public @NotNull List<String> getAddedTags() {
        return this.addedTags;
    }

    public @NotNull List<String> getRemovedTags() {
        return this.removedTags;
    }
}
