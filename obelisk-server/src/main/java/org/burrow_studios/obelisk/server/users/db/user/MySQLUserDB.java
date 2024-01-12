package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class MySQLUserDB extends SQLUserDB {
    private static final String URL = "jdbc:mysql://%s:%s/%s?allowMultiQueries=true";

    private static final String GET_USER_IDS = getStatementFromResources("/sql/entities/user/get_users.sql");
    private static final String GET_USER     = getStatementFromResources("/sql/entities/user/get_user.sql");
    private static final String GET_USER_DISCORD   = getStatementFromResources("/sql/entities/user/get_user_discord.sql");
    private static final String GET_USER_MINECRAFT = getStatementFromResources("/sql/entities/user/get_user_minecraft.sql");

    private static final String CREATE_USER = getStatementFromResources("/sql/entities/user/create_user.sql");;

    private static final String UPDATE_USER_NAME = getStatementFromResources("/sql/entities/user/update_user_name.sql");

    private static final String ADD_USER_DISCORD   = getStatementFromResources("/sql/entities/user/add_user_discord.sql");
    private static final String ADD_USER_MINECRAFT = getStatementFromResources("/sql/entities/user/add_user_minecraft.sql");

    private static final String REMOVE_USER_DISCORD   = getStatementFromResources("/sql/entities/user/remove_user_discord.sql");
    private static final String REMOVE_USER_MINECRAFT = getStatementFromResources("/sql/entities/user/remove_user_minecraft.sql");

    private final Connection connection;

    public MySQLUserDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = URL.formatted(host, port, database);

        final String tableStmt = getStatementFromResources("/sql/entities/user/tables_user.sql");

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            this.connection.createStatement().execute(tableStmt);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected @NotNull Set<Long> getUserIds0() throws SQLException {
        HashSet<Long> ids = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_USER_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                ids.add(result.getLong(1));
        }

        return Set.copyOf(ids);
    }

    @Override
    protected @NotNull JsonObject getUser0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_USER)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("name", result.getString("name"));
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_USER_DISCORD)) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getLong("discord"));

            json.add("discord", arr);
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_USER_MINECRAFT)) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getString("minecraft"));

            json.add("minecraft", arr);
        }

        return json;
    }

    @Override
    protected void createUser0(long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_USER)) {
            stmt.setLong(1, id);
            stmt.setString(2, name);

            stmt.execute();
        }
    }

    @Override
    protected void updateUserName0(long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_USER_NAME)) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void addUserDiscordId0(long user, long snowflake) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_USER_DISCORD)) {
            stmt.setLong(1, user);
            stmt.setLong(2, snowflake);

            stmt.execute();
        }
    }

    @Override
    protected void removeUserDiscordId0(long user, long snowflake) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_USER_DISCORD)) {
            stmt.setLong(1, user);
            stmt.setLong(2, snowflake);

            stmt.execute();
        }
    }

    @Override
    protected void addUserMinecraftId0(long user, @NotNull UUID uuid) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_USER_MINECRAFT)) {
            stmt.setLong(1, user);
            stmt.setString(2, uuid.toString());

            stmt.execute();
        }
    }

    @Override
    protected void removeUserMinecraftId0(long user, @NotNull UUID uuid) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_USER_MINECRAFT)) {
            stmt.setLong(1, user);
            stmt.setString(2, uuid.toString());

            stmt.execute();
        }
    }

    @Override
    protected void deleteUser0(long id) throws SQLException {
        // not implemented
    }
}
