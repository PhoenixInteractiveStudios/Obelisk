package org.burrow_studios.obelisk.api.entity;

import org.burrow_studios.obelisk.api.entity.dao.ProjectDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class Project {
    private final int id;
    private final ProjectDAO dao;

    public Project(int id, @NotNull ProjectDAO dao) {
        this.id = id;
        this.dao = dao;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull String getProjectTitle() {
        return this.dao.getProjectTitle(this.id);
    }

    public @Nullable String getApplicationTemplate() {
        return this.dao.getProjectApplicationTemplate(this.id);
    }

    public boolean isInviteOnly() {
        return this.dao.isProjectInviteOnly(this.id);
    }

    public @NotNull Collection<? extends User> getMembers() {
        return this.dao.getProjectMembers(this.id);
    }

    public void setTitle(@NotNull String title) {
        this.dao.setProjectTitle(this.id, title);
    }

    public void addMember(@NotNull User user) {
        this.dao.addProjectMember(this.id, user);
    }

    public void removeMember(@NotNull User user) {
        this.dao.removeProjectMember(this.id, user);
    }

    public void delete() {
        this.dao.deleteProject(this.id);
    }
}
