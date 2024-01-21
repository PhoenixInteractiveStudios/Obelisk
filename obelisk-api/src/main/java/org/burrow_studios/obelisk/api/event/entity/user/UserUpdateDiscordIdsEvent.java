package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class UserUpdateDiscordIdsEvent extends UserUpdateEvent<List<Long>> {
    private final @NotNull List<Long>   addedIds;
    private final @NotNull List<Long> removedIds;

    public UserUpdateDiscordIdsEvent(long id, @NotNull User entity, @NotNull List<Long> oldValue, @NotNull List<Long> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedIds = newValue.stream()
                .filter(discordId -> !oldValue.contains(discordId))
                .toList();
        this.removedIds = oldValue.stream()
                .filter(discordId -> !newValue.contains(discordId))
                .toList();
    }

    public @NotNull List<Long> getAddedIds() {
        return this.addedIds;
    }

    public @NotNull List<Long> getRemovedIds() {
        return this.removedIds;
    }
}
