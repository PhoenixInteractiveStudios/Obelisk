package org.burrow_studios.obelisk.server.moderation.db.project;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.moderation.ModerationService;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Set;

public interface ProjectDB {
    static @NotNull ProjectDB get(@NotNull ModerationService modService) {
        final String host     = "null";
        final int    port     = 3306;
        final String database = "null";
        final String user     = "null";
        final String pass     = "null";

        return new MySQLProjectDB(host, port, database, user, pass);
    }

    @NotNull Set<Long> getProjectIds() throws DatabaseException;

    @NotNull JsonObject getProject(long id) throws DatabaseException;

    void createProject(long id, @NotNull String title, @NotNull ProjectState state) throws DatabaseException;

    void updateProjectTitle(long id, @NotNull String title) throws DatabaseException;

    void addProjectTiming(long id, @NotNull String name, @NotNull Instant time) throws DatabaseException;

    void removeProjectTiming(long id, @NotNull String name) throws DatabaseException;

    void updateProjectState(long id, @NotNull ProjectState state) throws DatabaseException;

    void addProjectMember(long project, long user) throws DatabaseException;

    void removeProjectMember(long project, long user) throws DatabaseException;
}