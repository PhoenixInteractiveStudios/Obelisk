package org.burrow_studios.obelisk.api.entity.dao;

import org.burrow_studios.obelisk.api.entity.Project;
import org.burrow_studios.obelisk.api.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProjectDAO {
    @NotNull Project createProject(@NotNull String title, @Nullable String applicationTemplate, boolean inviteOnly);

    @NotNull List<? extends Project> listProjects();
    @NotNull Optional<Project> getProject(int id);
    @NotNull String getProjectTitle(int id);
    @Nullable String getProjectApplicationTemplate(int id);
    boolean isProjectInviteOnly(int id);
    @NotNull Collection<? extends User> getProjectMembers(int id);

    void setProjectTitle(int id, @NotNull String title);
    void addProjectMember(int id, @NotNull User user);
    void removeProjectMember(int id, @NotNull User user);

    void deleteProject(int id);
}
