package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.monolith.action.DatabaseModifier;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.jetbrains.annotations.NotNull;

public class DatabaseProjectModifier extends DatabaseModifier<Project> implements ProjectModifier {
    private String title;
    private Project.State state;

    public DatabaseProjectModifier(@NotNull BackendProject entity) {
        super(entity);
    }

    @Override
    public @NotNull DatabaseProjectModifier setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull DatabaseProjectModifier setState(@NotNull Project.State state) {
        this.state = state;
        return this;
    }

    public Project.State getState() {
        return this.state;
    }
}
