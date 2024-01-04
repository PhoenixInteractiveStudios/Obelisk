package org.burrow_studios.obelisk.server.db.dedicated.project;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.dedicated.SQLDB;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;

abstract class SQLProjectDB extends SQLDB implements ProjectDB {
    @Override
    public final @NotNull Set<Long> getProjectIds() throws DatabaseException {
        return this.wrap(this::getProjectIds0);
    }

    protected abstract @NotNull Set<Long> getProjectIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getProject(long id) throws DatabaseException {
        return this.wrap(() -> this.getProject0(id));
    }

    protected abstract @NotNull JsonObject getProject0(long id) throws SQLException;

    @Override
    public final void createProject(long id, @NotNull String title, int position) throws DatabaseException {
        this.wrap(() -> this.createProject0(id, title, position));
    }

    protected abstract void createProject0(long id, @NotNull String title, int position) throws SQLException;

    @Override
    public final void updateProjectTitle(long id, @NotNull String title) throws DatabaseException {
        this.wrap(() -> this.updateProjectTitle0(id, title));
    }

    protected abstract void updateProjectTitle0(long id, @NotNull String title) throws SQLException;

    @Override
    public final void updateProjectTiming(long id, @NotNull String key, @NotNull Instant time) throws DatabaseException {
        this.wrap(() -> this.updateProjectTiming0(id, key, time));
    }

    protected abstract void updateProjectTiming0(long id, @NotNull String key, @NotNull Instant time) throws SQLException;

    @Override
    public final void removeProjectTiming(long id, @NotNull String key) throws DatabaseException {
        this.wrap(() -> this.removeProjectTiming0(id, key));
    }

    protected abstract void removeProjectTiming0(long id, @NotNull String key) throws SQLException;

    @Override
    public final void updateProjectState(long id, @NotNull ProjectState state) throws DatabaseException {
        this.wrap(() -> this.updateProjectState0(id, state));
    }

    protected abstract void updateProjectState0(long id, @NotNull ProjectState state) throws SQLException;

    @Override
    public final void addProjectMember(long project, long user) throws DatabaseException {
        this.wrap(() -> this.addProjectMember0(project, user));
    }

    protected abstract void addProjectMember0(long project, long user) throws SQLException;

    @Override
    public final void removeProjectMember(long project, long user) throws DatabaseException {
        this.wrap(() -> this.removeProjectMember0(project, user));
    }

    protected abstract void removeProjectMember0(long project, long user) throws SQLException;
}
