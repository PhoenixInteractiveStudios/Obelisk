package org.burrow_studios.obelisk.api.event.entity.project;

import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class ProjectMemberAddEvent extends ProjectEvent {
    private final @NotNull User member;

    public ProjectMemberAddEvent(long id, @NotNull Project entity, @NotNull User member) {
        super(id, entity);
        this.member = member;
    }

    public @NotNull User getMember() {
        return member;
    }
}
