package org.burrow_studios.obelisk.monolith.action.entity.project;

import org.burrow_studios.obelisk.api.action.entity.project.ProjectModifier;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.monolith.action.DatabaseModifier;
import org.burrow_studios.obelisk.monolith.db.IActionableDatabase;
import org.burrow_studios.obelisk.monolith.entities.BackendProject;
import org.burrow_studios.obelisk.monolith.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DatabaseProjectModifier extends DatabaseModifier<Project> implements ProjectModifier {
    private String title;
    private Project.State state;

    public DatabaseProjectModifier(@NotNull BackendProject entity) {
        super(entity);
    }

    @Override
    public void execute(@NotNull IActionableDatabase actionableDatabase, @NotNull CompletableFuture<Project> future) throws DatabaseException {
        actionableDatabase.modifyProject(this);
        future.complete(null);
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
