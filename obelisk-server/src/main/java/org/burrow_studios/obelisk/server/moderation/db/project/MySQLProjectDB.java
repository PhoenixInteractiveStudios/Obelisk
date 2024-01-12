package org.burrow_studios.obelisk.server.moderation.db.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

class MySQLProjectDB extends SQLProjectDB {
    private static final String URL = "jdbc:mysql://%s:%s/%s?allowMultiQueries=true";

    public MySQLProjectDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        super(URL.formatted(host, port, database), user, pass, "project/tables_project");
    }

    @Override
    protected @NotNull Set<Long> getProjectIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("project/get_projects")) {
            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getProject0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("project/get_project")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        }

        try (PreparedStatement stmt = prepareStatement("project/get_project_timings")) {
            stmt.setLong(1, id);

            JsonObject timings = new JsonObject();

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                final String  name = result.getString("name");
                final Instant time = result.getTimestamp("time").toInstant();

                timings.addProperty(name, time.toString());
            }

            json.add("timings", timings);
        }

        try (PreparedStatement stmt = prepareStatement("project/get_project_members")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getLong("user"));

            json.add("members", arr);
        }

        return json;
    }

    @Override
    protected void createProject0(long id, @NotNull String title, @NotNull ProjectState state) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/create_project")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        }
    }

    @Override
    protected void updateProjectTitle0(long id, @NotNull String title) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/update_project_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addProjectTiming0(long id, @NotNull String name, @NotNull Instant time) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/add_project_timing")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setTimestamp(3, Timestamp.from(time));

            stmt.execute();
        }
    }

    @Override
    protected void removeProjectTiming0(long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/remove_project_timing")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);

            stmt.execute();
        }
    }

    @Override
    protected void updateProjectState0(long id, @NotNull ProjectState state) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/update_project_state")) {
            stmt.setString(1, state.name());
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addProjectMember0(long project, long user) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/add_project_member")) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void removeProjectMember0(long project, long user) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("project/remove_project_member")) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }
}
