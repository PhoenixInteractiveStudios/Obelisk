package org.burrow_studios.obelisk.server.db.dedicated.project;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;

abstract class SQLProjectDB implements ProjectDB {
    @Override
    public final @NotNull Set<Long> getProjectIds() throws DatabaseException {
        try {
            return this.getProjectIds0();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getProjectIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getProject(long id) throws DatabaseException {
        try {
            return this.getProject0(id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getProject0(long id) throws SQLException;

    @Override
    public final void createProject(long id, @NotNull String title, int position) throws DatabaseException {
        try {
            this.createProject0(id, title, position);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createProject0(long id, @NotNull String title, int position) throws SQLException;

    @Override
    public final void updateProjectTitle(long id, @NotNull String title) throws DatabaseException {
        try {
            this.updateProjectTitle0(id, title);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateProjectTitle0(long id, @NotNull String title) throws SQLException;

    @Override
    public final void updateProjectTiming(long id, @NotNull String key, @NotNull Instant time) throws DatabaseException {
        try {
            this.updateProjectTiming0(id, key, time);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateProjectTiming0(long id, @NotNull String key, @NotNull Instant time) throws SQLException;

    @Override
    public final void removeProjectTiming(long id, @NotNull String key) throws DatabaseException {
        try {
            this.removeProjectTiming0(id, key);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeProjectTiming0(long id, @NotNull String key) throws SQLException;

    @Override
    public final void updateProjectState(long id, @NotNull ProjectState state) throws DatabaseException {
        try {
            this.updateProjectState0(id, state);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateProjectState0(long id, @NotNull ProjectState state) throws SQLException;

    @Override
    public final void addProjectMember(long project, long user) throws DatabaseException {
        try {
            this.addProjectMember0(project, user);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void addProjectMember0(long project, long user) throws SQLException;

    @Override
    public final void removeProjectMember(long project, long user) throws DatabaseException {
        try {
            this.removeProjectMember0(project, user);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeProjectMember0(long project, long user) throws SQLException;
}
