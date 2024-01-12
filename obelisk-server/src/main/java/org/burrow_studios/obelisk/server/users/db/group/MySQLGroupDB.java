package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

class MySQLGroupDB extends SQLGroupDB {
    private static final String URL = "jdbc:mysql://{0}:{1}/{2}";

    private static final String CREATE_TABLE_GROUPS        = "CREATE TABLE IF NOT EXISTS `groups` (`id` BIGINT(20) NOT NULL, `name` TEXT NOT NULL, `position` INT NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_GROUP_MEMBERS = "CREATE TABLE IF NOT EXISTS `group_members` (`group` BIGINT(20) NOT NULL, `user` BIGINT(20) NOT NULL, PRIMARY KEY (`group`, `user`));";
    private static final String ALTER_TABLE_GROUP_MEMBERS  = "ALTER TABLE `group_members` ADD FOREIGN KEY (`group`) REFERENCES `groups`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";

    private static final String GET_GROUP_IDS = "SELECT `id` FROM `groups`;";
    private static final String GET_GROUP     = "SELECT * FROM `groups` WHERE `id` = ?;";
    private static final String GET_GROUP_MEMBERS = "SELECT `user` FROM `group_members` WHERE `group` = ?;";

    private static final String CREATE_GROUP = "INSERT INTO `groups` (`id`, `name`, `position`) VALUES ('?', '?', '?');";

    private static final String UPDATE_GROUP_NAME     = "UPDATE `groups` SET `name` = '?' WHERE `id` = ?;";
    private static final String UPDATE_GROUP_POSITION = "UPDATE `groups` SET `position` = ? WHERE `id` = ?;";

    private static final String ADD_GROUP_MEMBER = "INSERT INTO `group_members` (`group`, `user`) VALUES ('?', '?');";
    private static final String REMOVE_GROUP_MEMBER = "DELETE FROM `group_members` WHERE `group` = ? AND `user` = ?;";

    private final Connection connection;

    public MySQLGroupDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = MessageFormat.format(URL, host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            final PreparedStatement createGroups       = this.connection.prepareStatement(CREATE_TABLE_GROUPS);
            final PreparedStatement createGroupMembers = this.connection.prepareStatement(CREATE_TABLE_GROUP_MEMBERS);
            final PreparedStatement alterGroupMembers  = this.connection.prepareStatement(ALTER_TABLE_GROUP_MEMBERS);

            createGroups.execute();
            createGroupMembers.execute();
            alterGroupMembers.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected @NotNull Set<Long> getGroupIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_GROUP_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getGroup0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_GROUP)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("name", result.getString("name"));
            json.addProperty("position", result.getInt("position"));
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_GROUP_MEMBERS)) {
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
    protected void createGroup0(long id, @NotNull String name, int position) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_GROUP)) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, position);

            stmt.execute();
        }
    }

    @Override
    protected void updateGroupName0(long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_GROUP_NAME)) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateGroupPosition0(long id, int position) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_GROUP_POSITION)) {
            stmt.setInt(1, position);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addGroupMember0(long group, long user) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_GROUP_MEMBER)) {
            stmt.setLong(1, group);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void removeGroupMember0(long group, long user) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_GROUP_MEMBER)) {
            stmt.setLong(1, group);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void deleteGroup0(long id) throws SQLException {
        // not implemented
    }
}
