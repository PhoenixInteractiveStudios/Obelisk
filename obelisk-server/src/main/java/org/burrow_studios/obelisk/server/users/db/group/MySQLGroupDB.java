package org.burrow_studios.obelisk.server.users.db.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class MySQLGroupDB extends SQLGroupDB {
    private static final String URL = "jdbc:mysql://%s:%s/%s?allowMultiQueries=true";

    private static final String GET_GROUP_IDS = getStatementFromResources("/sql/entities/group/get_groups.sql");
    private static final String GET_GROUP     = getStatementFromResources("/sql/entities/group/get_group.sql");
    private static final String GET_GROUP_MEMBERS = getStatementFromResources("/sql/entities/group/get_group_members.sql");

    private static final String CREATE_GROUP = getStatementFromResources("/sql/entities/group/create_group.sql");

    private static final String UPDATE_GROUP_NAME     = getStatementFromResources("/sql/entities/group/update_group_name.sql");
    private static final String UPDATE_GROUP_POSITION = getStatementFromResources("/sql/entities/group/update_group_position.sql");

    private static final String ADD_GROUP_MEMBER = getStatementFromResources("/sql/entities/group/add_group_member.sql");
    private static final String REMOVE_GROUP_MEMBER = getStatementFromResources("/sql/entities/group/remove_group_member.sql");

    private final Connection connection;

    public MySQLGroupDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = URL.formatted(host, port, database);

        final String tableStmt = getStatementFromResources("/sql/entities/group/tables_group.sql");

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            this.connection.createStatement().execute(tableStmt);
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
