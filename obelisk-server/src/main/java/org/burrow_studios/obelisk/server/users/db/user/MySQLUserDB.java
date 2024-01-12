package org.burrow_studios.obelisk.server.users.db.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class MySQLUserDB extends SQLUserDB {
    private static final String URL = "jdbc:mysql://{0}:{1}/{2}";

    private static final String CREATE_TABLE_USERS          = "CREATE TABLE IF NOT EXISTS `users` (`id` BIGINT(20) NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_USER_DISCORD   = "CREATE TABLE IF NOT EXISTS `user_discord` (`user` BIGINT(20) NOT NULL, `discord` BIGINT(20) NOT NULL, PRIMARY KEY (`user`, `discord`), UNIQUE(`discord`));";
    private static final String CREATE_TABLE_USER_MINECRAFT = "CREATE TABLE IF NOT EXISTS `user_minecraft` (`user` BIGINT(20) NOT NULL, `minecraft` UUID NOT NULL, PRIMARY KEY (`user`, `minecraft`), UNIQUE(`minecraft`));";
    private static final String ALTER_TABLE_USER_DISCORD   = "ALTER TABLE `user_discord` ADD FOREIGN KEY (`user`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";
    private static final String ALTER_TABLE_USER_MINECRAFT = "ALTER TABLE `user_minecraft` ADD FOREIGN KEY (`user`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";

    private static final String GET_USER_IDS = "SELECT `id` FROM `users`;";
    private static final String GET_USER     = "SELECT * FROM `users` WHERE `id` = ?;";
    private static final String GET_USER_DISCORD   = "SELECT `discord` FROM `user_discord` WHERE `user` = ?;";
    private static final String GET_USER_MINECRAFT = "SELECT `minecraft` FROM `user_minecraft` WHERE `user` = ?;";

    private static final String CREATE_USER = "INSERT INTO `users` (`id`, `name`) VALUES ('?', '?');";

    private static final String UPDATE_USER_NAME = "UPDATE `users` SET `name` = '?' WHERE `id` = ?;";

    private static final String ADD_USER_DISCORD   = "INSERT INTO `user_discord` (`user`, `discord`) VALUES ('?', '?');";
    private static final String ADD_USER_MINECRAFT = "INSERT INTO `user_minecraft` (`user`, `minecraft`) VALUES ('?', '?');";

    private static final String REMOVE_USER_DISCORD   = "DELETE FROM `user_discord` WHERE `user` = ? AND `discord` = ?;";
    private static final String REMOVE_USER_MINECRAFT = "DELETE FROM `user_minecraft` WHERE `user` = ? AND `minecraft` = ?;";

    private final Connection connection;

    public MySQLUserDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = MessageFormat.format(URL, host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            final PreparedStatement createUsers         = this.connection.prepareStatement(CREATE_TABLE_USERS);
            final PreparedStatement createUserDiscord   = this.connection.prepareStatement(CREATE_TABLE_USER_DISCORD);
            final PreparedStatement createUserMinecraft = this.connection.prepareStatement(CREATE_TABLE_USER_MINECRAFT);

            final PreparedStatement alterUserDiscord   = this.connection.prepareStatement(ALTER_TABLE_USER_DISCORD);
            final PreparedStatement alterUserMinecraft = this.connection.prepareStatement(ALTER_TABLE_USER_MINECRAFT);

            createUsers.execute();
            createUserDiscord.execute();
            createUserMinecraft.execute();
            alterUserDiscord.execute();
            alterUserMinecraft.execute();
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
