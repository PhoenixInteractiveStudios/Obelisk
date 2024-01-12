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

    public MySQLGroupDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        super(URL.formatted(host, port, database), user, pass, "group/tables_group");
    }

    @Override
    protected @NotNull Set<Long> getGroupIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("group/get_groups")) {
            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getGroup0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("group/get_group")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("name", result.getString("name"));
            json.addProperty("position", result.getInt("position"));
        }

        try (PreparedStatement stmt = prepareStatement("group/get_group_members")) {
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
        try (PreparedStatement stmt = prepareStatement("group/create_group")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, position);

            stmt.execute();
        }
    }

    @Override
    protected void updateGroupName0(long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("group/update_group_name")) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateGroupPosition0(long id, int position) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("group/update_group_position")) {
            stmt.setInt(1, position);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addGroupMember0(long group, long user) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("group/add_group_member")) {
            stmt.setLong(1, group);
            stmt.setLong(2, user);

            stmt.execute();
        }
    }

    @Override
    protected void removeGroupMember0(long group, long user) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("group/remove_group_member")) {
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
