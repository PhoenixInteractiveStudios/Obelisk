package org.burrow_studios.obelisk.server.moderation.db.project;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;

class SQLiteProjectDB extends SQLProjectDB {
    @Override
    protected @NotNull Set<Long> getProjectIds0() throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getProject0(long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createProject0(long id, @NotNull String title, int position) throws SQLException {
        // TODO
    }

    @Override
    protected void updateProjectTitle0(long id, @NotNull String title) throws SQLException {
        // TODO
    }

    @Override
    protected void updateProjectTiming0(long id, @NotNull String key, @NotNull Instant time) throws SQLException {
        // TODO
    }

    @Override
    protected void removeProjectTiming0(long id, @NotNull String key) throws SQLException {
        // TODO
    }

    @Override
    protected void updateProjectState0(long id, @NotNull ProjectState state) throws SQLException {
        // TODO
    }

    @Override
    protected void addProjectMember0(long project, long user) throws SQLException {
        // TODO
    }

    @Override
    protected void removeProjectMember0(long project, long user) throws SQLException {
        // TODO
    }
}
