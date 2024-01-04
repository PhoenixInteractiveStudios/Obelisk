package org.burrow_studios.obelisk.server.db.dedicated.project;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;

public interface ProjectDB {
    static @NotNull ProjectDB get() {
        return new SQLiteProjectDB();
    }

    @NotNull Set<Long> getProjectIds() throws DatabaseException;

    @NotNull JsonObject getProject(long id) throws DatabaseException;

    void createProject(long id, @NotNull String title, int position) throws DatabaseException;

    void updateProjectTitle(long id, @NotNull String title) throws DatabaseException;

    void updateProjectTiming(long id, @NotNull String key, @NotNull Instant time) throws DatabaseException;

    void removeProjectTiming(long id, @NotNull String key) throws DatabaseException;

    void updateProjectState(long id, @NotNull ProjectState state) throws DatabaseException;

    void addProjectMember(long project, long user) throws DatabaseException;

    void removeProjectMember(long project, long user) throws DatabaseException;
}
