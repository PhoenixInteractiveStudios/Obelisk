package org.burrow_studios.obelisk.userservice.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.userservice.Main;
import org.burrow_studios.obelisk.userservice.exceptions.DatabaseException;
import org.burrow_studios.obelisk.userservice.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityDatabase implements Closeable, GroupDB, UserDB {
    private static final String URL = "jdbc:mysql://%s:%s/%s?allowMultiQueries=true";

    private final ConcurrentHashMap<String, String> statements = new ConcurrentHashMap<>();
    private final Connection connection;

    public EntityDatabase(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = URL.formatted(host, port, database);

        DriverManager.setLoginTimeout(8);

        try {
            this.connection = DriverManager.getConnection(url, user, pass);
            this.execute("tables");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private String getStatement(@NotNull String key) throws DatabaseException {
        String stmt = statements.get(key);
        if (stmt != null)
            return stmt;

        final String resource = "/sql/entities/" + key + ".sql";
        try {
            java.net.URL res = Main.class.getResource(resource);
            if (res == null)
                throw new DatabaseException("Statement does not exist in resources: " + resource);
            stmt = Files.readString(Path.of(res.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new DatabaseException("Could not load statement from resources: " + resource, e);
        }

        statements.put(key, stmt);
        return stmt;
    }

    private @NotNull PreparedStatement prepareStatement(@NotNull String key) throws SQLException, DatabaseException {
        return this.connection.prepareStatement(getStatement(key));
    }

    private @NotNull ResultSet executeQuery(@NotNull String key) throws SQLException, DatabaseException {
        return this.connection.createStatement().executeQuery(getStatement(key));
    }

    private void execute(@NotNull String key) throws SQLException, DatabaseException {
        this.connection.createStatement().execute(getStatement(key));
    }

    /* - - - - - - - - - - */

    // GROUP

    @Override
    public @NotNull Set<Long> getGroupIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("group/get_groups")) {
            while (result.next())
                ids.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getGroup(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("group/get_group")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("name", result.getString("name"));
            json.addProperty("position", result.getInt("position"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("group/get_group_members")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getLong("user"));

            json.add("members", arr);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return json;
    }

    @Override
    public void createGroup(long id, @NotNull String name, int position) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("group/create_group")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, position);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateGroupName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("group/update_group_name")) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateGroupPosition(long id, int position) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("group/update_group_position")) {
            stmt.setInt(1, position);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addGroupMember(long group, long user) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("group/add_group_member")) {
            stmt.setLong(1, group);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeGroupMember(long group, long user) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("group/remove_group_member")) {
            stmt.setLong(1, group);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteGroup(long id) throws DatabaseException {
        // TODO
    }

    // USERS

    @Override
    public @NotNull Set<Long> getUserIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("user/get_users")) {
            while (result.next())
                ids.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getUser(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("user/get_user")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("name", result.getString("name"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("user/get_user_discord")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getLong("discord"));

            json.add("discord", arr);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("user/get_user_minecraft")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getString("minecraft"));

            json.add("minecraft", arr);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return json;
    }

    @Override
    public void createUser(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("user/create_user")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateUserName(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("user/update_user_name")) {
            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addUserDiscordId(long user, long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("user/add_user_discord")) {
            stmt.setLong(1, user);
            stmt.setLong(2, snowflake);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeUserDiscordId(long user, long snowflake) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("user/remove_user_discord")) {
            stmt.setLong(1, user);
            stmt.setLong(2, snowflake);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("user/add_user_minecraft")) {
            stmt.setLong(1, user);
            stmt.setString(2, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeUserMinecraftId(long user, @NotNull UUID uuid) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("user/remove_user_minecraft")) {
            stmt.setLong(1, user);
            stmt.setString(2, uuid.toString());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteUser(long id) throws DatabaseException {
        // TODO
    }
}
