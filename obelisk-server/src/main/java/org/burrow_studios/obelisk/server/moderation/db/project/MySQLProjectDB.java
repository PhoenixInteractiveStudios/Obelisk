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

    private static final String GET_PROJECT_IDS = getStatementFromResources("/sql/entities/project/get_projects.sql");
    private static final String GET_PROJECT     = getStatementFromResources("/sql/entities/project/get_project.sql");
    private static final String GET_PROJECT_TIMINGS = getStatementFromResources("/sql/entities/project/get_project_timings.sql");
    private static final String GET_PROJECT_MEMBERS = getStatementFromResources("/sql/entities/project/get_project_members.sql");

    private static final String CREATE_PROJECT = getStatementFromResources("/sql/entities/project/create_project.sql");

    private static final String UPDATE_PROJECT_TITLE = getStatementFromResources("/sql/entities/project/update_project_title.sql");
    private static final String UPDATE_PROJECT_STATE = getStatementFromResources("/sql/entities/project/update_project_state.sql");

    private static final String ADD_PROJECT_TIMING = getStatementFromResources("/sql/entities/project/add_project_timing.sql");
    private static final String ADD_PROJECT_MEMBER = getStatementFromResources("/sql/entities/project/add_project_member.sql");

    private static final String REMOVE_PROJECT_TIMING = getStatementFromResources("/sql/entities/project/remove_project_timing.sql");
    private static final String REMOVE_PROJECT_MEMBER = getStatementFromResources("/sql/entities/project/remove_project_member.sql");

    private final Connection connection;

    public MySQLProjectDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = URL.formatted(host, port, database);

        final String tableStmt = getStatementFromResources("/sql/entities/project/tables_project.sql");

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            this.connection.createStatement().execute(tableStmt);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected @NotNull Set<Long> getProjectIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_PROJECT_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getProject0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_PROJECT)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_PROJECT_TIMINGS)) {
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

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_PROJECT_MEMBERS)) {
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
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_PROJECT)) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        }
    }

    @Override
    protected void updateProjectTitle0(long id, @NotNull String title) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_PROJECT_TITLE)) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addProjectTiming0(long id, @NotNull String name, @NotNull Instant time) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_PROJECT_TIMING)) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setTimestamp(3, Timestamp.from(time));

            stmt.execute();
        }
    }

    @Override
    protected void removeProjectTiming0(long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_PROJECT_TIMING)) {
            stmt.setLong(1, id);
            stmt.setString(2, name);

            stmt.execute();
        }
    }

    @Override
    protected void updateProjectState0(long id, @NotNull ProjectState state) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_PROJECT_STATE)) {
            stmt.setString(1, state.name());
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addProjectMember0(long project, long user) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_PROJECT_MEMBER)) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void removeProjectMember0(long project, long user) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_PROJECT_MEMBER)) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }
}
