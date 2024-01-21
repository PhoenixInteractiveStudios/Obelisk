package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class UserUpdateMinecraftIdsEvent extends UserUpdateEvent<List<UUID>> {
    private final @NotNull List<UUID>   addedIds;
    private final @NotNull List<UUID> removedIds;

    public UserUpdateMinecraftIdsEvent(long id, @NotNull User entity, @NotNull List<UUID> oldValue, @NotNull List<UUID> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedIds = newValue.stream()
                .filter(minecraftId -> !oldValue.contains(minecraftId))
                .toList();
        this.removedIds = oldValue.stream()
                .filter(minecraftId -> !newValue.contains(minecraftId))
                .toList();
    }

    public @NotNull List<UUID> getAddedIds() {
        return this.addedIds;
    }

    public @NotNull List<UUID> getRemovedIds() {
        return this.removedIds;
    }
}
