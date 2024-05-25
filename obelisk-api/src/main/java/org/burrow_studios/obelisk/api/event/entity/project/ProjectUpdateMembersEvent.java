package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class ProjectUpdateMembersEvent extends ProjectUpdateEvent<Set<User>> {
    private final @NotNull Set<User>   addedMembers;
    private final @NotNull Set<User> removedMembers;

    public ProjectUpdateMembersEvent(long id, @NotNull Project entity, @NotNull Set<User> oldValue, @NotNull Set<User> newValue) {
        super(id, entity, oldValue, newValue);

        this.addedMembers = newValue.stream()
                .filter(user -> !oldValue.contains(user))
                .collect(Collectors.toUnmodifiableSet());
        this.removedMembers = oldValue.stream()
                .filter(user -> !newValue.contains(user))
                .collect(Collectors.toUnmodifiableSet());
    }

    public @NotNull Set<User> getAddedMembers() {
        return this.addedMembers;
    }

    public @NotNull Set<User> getRemovedMembers() {
        return this.removedMembers;
    }
}
