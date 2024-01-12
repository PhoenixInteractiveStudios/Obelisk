package org.burrow_studios.obelisk.server.moderation.db.project;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.SQLDB;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;

abstract class SQLProjectDB extends SQLDB implements ProjectDB {
    public SQLProjectDB(@NotNull String url, @NotNull String user, @NotNull String pass, @NotNull String tableKey) throws DatabaseException {
        super(url, user, pass, tableKey);
    }

    @Override
    public final @NotNull Set<Long> getProjectIds() throws DatabaseException {
        return this.wrap(this::getProjectIds0);
    }

    @Override
    public final @NotNull JsonObject getProject(long id) throws DatabaseException {
        return this.wrap(() -> this.getProject0(id));
    }

    @Override
    public final void createProject(long id, @NotNull String title, @NotNull ProjectState state) throws DatabaseException {
        this.wrap(() -> this.createProject0(id, title, state));
    }

    @Override
    public final void updateProjectTitle(long id, @NotNull String title) throws DatabaseException {
        this.wrap(() -> this.updateProjectTitle0(id, title));
    }

    @Override
    public final void addProjectTiming(long id, @NotNull String name, @NotNull Instant time) throws DatabaseException {
        this.wrap(() -> this.addProjectTiming0(id, name, time));
    }

    @Override
    public final void removeProjectTiming(long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.removeProjectTiming0(id, name));
    }

    @Override
    public final void updateProjectState(long id, @NotNull ProjectState state) throws DatabaseException {
        this.wrap(() -> this.updateProjectState0(id, state));
    }

    @Override
    public final void addProjectMember(long project, long user) throws DatabaseException {
        this.wrap(() -> this.addProjectMember0(project, user));
    }

    @Override
    public final void removeProjectMember(long project, long user) throws DatabaseException {
        this.wrap(() -> this.removeProjectMember0(project, user));
    }

    protected abstract @NotNull Set<Long> getProjectIds0() throws SQLException;

    protected abstract @NotNull JsonObject getProject0(long id) throws SQLException;

    protected abstract void createProject0(long id, @NotNull String title, @NotNull ProjectState state) throws SQLException;

    protected abstract void updateProjectTitle0(long id, @NotNull String title) throws SQLException;

    protected abstract void addProjectTiming0(long id, @NotNull String name, @NotNull Instant time) throws SQLException;

    protected abstract void removeProjectTiming0(long id, @NotNull String name) throws SQLException;

    protected abstract void updateProjectState0(long id, @NotNull ProjectState state) throws SQLException;

    protected abstract void addProjectMember0(long project, long user) throws SQLException;

    protected abstract void removeProjectMember0(long project, long user) throws SQLException;
}
