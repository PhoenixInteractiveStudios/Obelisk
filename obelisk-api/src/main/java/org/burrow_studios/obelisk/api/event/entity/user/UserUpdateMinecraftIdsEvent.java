package org.burrow_studios.obelisk.api.event.entity.user;

import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserUpdateMinecraftIdsEvent extends UserUpdateEvent<Set<UUID>> {
    private final @NotNull Set<UUID>   addedIds;
    private final @NotNull Set<UUID> removedIds;

    public UserUpdateMinecraftIdsEvent(long id, @NotNull User entity, @NotNull Set<UUID> oldValue, @NotNull Set<UUID> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedIds = newValue.stream()
                .filter(minecraftId -> !oldValue.contains(minecraftId))
                .collect(Collectors.toUnmodifiableSet());
        this.removedIds = oldValue.stream()
                .filter(minecraftId -> !newValue.contains(minecraftId))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<UUID> getAddedIds() {
        return this.addedIds;
    }

    public @NotNull Set<UUID> getRemovedIds() {
        return this.removedIds;
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.USER_UPDATE_MINECRAFT_IDS_EVENT;
    }
}
