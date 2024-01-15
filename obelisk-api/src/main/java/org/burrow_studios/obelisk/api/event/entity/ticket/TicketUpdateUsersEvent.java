package org.burrow_studios.obelisk.api.event.entity.ticket;

import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.api.event.GatewayOpcodes;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public final class TicketUpdateUsersEvent extends TicketUpdateEvent<Set<User>> {
    private final @NotNull Set<User>   addedUsers;
    private final @NotNull Set<User> removedUsers;

    public TicketUpdateUsersEvent(long id, @NotNull Ticket entity, @NotNull Set<User> oldValue, @NotNull Set<User> newValue) {
        super(id, entity, oldValue, newValue);
        this.addedUsers = newValue.stream()
                .filter(user -> !oldValue.contains(user))
                .collect(Collectors.toUnmodifiableSet());
        this.removedUsers = oldValue.stream()
                .filter(user -> !newValue.contains(user))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<User> getAddedUsers() {
        return this.addedUsers;
    }

    public @NotNull Set<User> getRemovedUsers() {
        return this.removedUsers;
    }

    @Override
    public int getOpcode() {
        return GatewayOpcodes.TICKET_UPDATE_USERS_EVENT;
    }
}
