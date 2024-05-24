package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectBuilder;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.action.DatabaseBuilder;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseProjectBuilder extends DatabaseBuilder<Project> implements ProjectBuilder {
    private String title;
    private Project.State state;

    public DatabaseProjectBuilder(@NotNull ObeliskMonolith obelisk) {
        super(obelisk);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Project> future) throws DatabaseException {
        BackendProject project = actionableDatabase.onProjectBuild(this);
        future.complete(project);
    }

    @Override
    public @NotNull DatabaseProjectBuilder setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public @NotNull DatabaseProjectBuilder setState(@NotNull Project.State state) {
        this.state = state;
        return this;
    }

    public Project.State getState() {
        return this.state;
    }
}
