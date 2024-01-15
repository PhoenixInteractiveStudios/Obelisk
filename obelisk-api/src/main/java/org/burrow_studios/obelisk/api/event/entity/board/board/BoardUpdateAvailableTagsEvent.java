package org.burrow_studios.obelisk.api.event.entity.board.board;

import org.burrow_studios.obelisk.api.entities.board.Board;
import org.burrow_studios.obelisk.api.entities.board.Tag;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class BoardUpdateAvailableTagsEvent extends BoardUpdateEvent<Set<Tag>> {
    private final @NotNull Set<Tag>   addedTags;
    private final @NotNull Set<Tag> removedTags;

    public BoardUpdateAvailableTagsEvent(long id, @NotNull Board entity, @NotNull Set<Tag> oldValue, @NotNull Set<Tag> newValue) {
        super(id, entity, oldValue, newValue);
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

    @Override
    public int getOpcode() {
        return GatewayOpcodes.BOARD_UPDATE_AVAILABLE_TAGS_EVENT;
    }
}
