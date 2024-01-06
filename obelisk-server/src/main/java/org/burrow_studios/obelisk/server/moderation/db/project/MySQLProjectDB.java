package org.burrow_studios.obelisk.server.moderation.db.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

class MySQLProjectDB extends SQLProjectDB {
    private static final String URL = "jdbc:mysql://{0}:{1}/{2}";

    private static final String CREATE_TABLE_PROJECTS        = "CREATE TABLE IF NOT EXISTS `projects` (`id` BIGINT(20) NOT NULL, `title` TEXT NOT NULL, `state` TEXT NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_PROJECT_TIMINGS = "CREATE TABLE IF NOT EXISTS `project_timings` (`project` BIGINT(20) NOT NULL, `name` VARCHAR(256) NOT NULL, `time` TIMESTAMP NOT NULL, PRIMARY KEY (`project`, `name`));";
    private static final String CREATE_TABLE_PROJECT_MEMBERS = "CREATE TABLE IF NOT EXISTS `project_members` (`project` BIGINT(20) NOT NULL, `member` BIGINT(20) NOT NULL, PRIMARY KEY (`project`, `member`));";
    private static final String ALTER_TABLE_PROJECT_TIMINGS = "ALTER TABLE `project_timings` ADD FOREIGN KEY (`project`) REFERENCES `projects`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";
    private static final String ALTER_TABLE_PROJECT_MEMBERS = "ALTER TABLE `project_members` ADD FOREIGN KEY (`project`) REFERENCES `projects`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";

    private static final String GET_PROJECT_IDS = "SELECT `id` FROM `projects`;";
    private static final String GET_PROJECT     = "SELECT * FROM `projects` WHERE `id` = ?;";
    private static final String GET_PROJECT_TIMINGS = "SELECT `name`, `time` FROM `project_timings` WHERE `project` = ?;";
    private static final String GET_PROJECT_MEMBERS = "SELECT `member` FROM `project_members` WHERE `project` = ?;";

    private static final String CREATE_PROJECT = "INSERT INTO `projects` (`id`, `title`, `state`) VALUES ('?', '?', '?');";

    private static final String UPDATE_PROJECT_TITLE = "UPDATE `projects` SET `title` = '?' WHERE `id` = ?;";
    private static final String UPDATE_PROJECT_STATE = "UPDATE `projects` SET `state` = '?' WHERE `id` = ?;";

    private static final String ADD_PROJECT_TIMING = "INSERT INTO `project_timings` (`project`, `name`, `time`) VALUES ('?', '?', '?');";
    private static final String ADD_PROJECT_MEMBER = "INSERT INTO `project_members` (`project`, `user`) VALUES ('?', '?');";

    private static final String REMOVE_PROJECT_TIMING = "DELETE FROM `project_timings` WHERE `project` = ? AND `name` = ?;";
    private static final String REMOVE_PROJECT_MEMBER = "DELETE FROM `project_members` WHERE `project` = ? AND `user` = ?;";

    private final Connection connection;

    public MySQLProjectDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = MessageFormat.format(URL, host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            final PreparedStatement createProjects       = this.connection.prepareStatement(CREATE_TABLE_PROJECTS);
            final PreparedStatement createProjectTimings = this.connection.prepareStatement(CREATE_TABLE_PROJECT_TIMINGS);
            final PreparedStatement createProjectMembers = this.connection.prepareStatement(CREATE_TABLE_PROJECT_MEMBERS);

            final PreparedStatement alterProjectTimings = this.connection.prepareStatement(ALTER_TABLE_PROJECT_TIMINGS);
            final PreparedStatement alterProjectMembers = this.connection.prepareStatement(ALTER_TABLE_PROJECT_MEMBERS);

            createProjects.execute();
            createProjectTimings.execute();
            createProjectMembers.execute();

            alterProjectTimings.execute();
            alterProjectMembers.execute();
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
    protected void updateProjectTiming0(long id, @NotNull String key, @NotNull Instant time) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_PROJECT_TIMING)) {
            stmt.setLong(1, id);
            stmt.setString(2, key);
            stmt.setTimestamp(3, Timestamp.from(time));

            stmt.execute();
        }
    }

    @Override
    protected void removeProjectTiming0(long id, @NotNull String key) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_PROJECT_TIMING)) {
            stmt.setLong(1, id);
            stmt.setString(2, key);

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
