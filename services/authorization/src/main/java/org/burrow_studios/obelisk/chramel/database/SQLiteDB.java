package org.burrow_studios.obelisk.chramel.database;

import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDB implements Database {
    private static final String STMT_CREATE_TABLE_INTENTS      = "CREATE TABLE IF NOT EXISTS `intents` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `intent` TEXT UNIQUE NOT NULL);";
    private static final String STMT_CREATE_TABLE_APPLICATIONS = "CREATE TABLE IF NOT EXISTS `applications` (`id` BIGINT(20) NOT NULL, `intent` INTEGER NOT NULL, PRIMARY KEY (`id`, `intent`));";

    private static final String STMT_SELECT_APPLICATION_INTENTS = "SELECT `intents`.`intent` FROM `applications` INNER JOIN `intents` ON `applications`.`intent` == `intents`.`id` WHERE `applications`.`id` == ?;";
    private static final String STMT_SELECT_APPLICATION_INTENT  = "SELECT COUNT(*) FROM `applications` INNER JOIN `intents` ON `applications`.`intent` == `intents`.`id` WHERE `applications`.`id` == ? AND `intents`.`intent` == ?;";

    private static final String STMT_INSERT_INTENT             = "INSERT OR IGNORE INTO `intents` (`intent`) VALUES(?);";
    private static final String STMT_INSERT_APPLICATION_INTENT = "INSERT INTO `applications` (`id`, `intent`) VALUES(?, ( SELECT `id` FROM `intents` WHERE `intent` = ?));";

    private static final Logger LOG = Logger.getLogger(SQLiteDB.class.getSimpleName());

    private final Connection connection;

    public SQLiteDB(@NotNull File file) throws SQLException {
        String url = String.format("jdbc:sqlite:%s", file.getAbsolutePath());

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);

        LOG.log(Level.INFO, "Initiating database connection to " + url);
        this.connection = DriverManager.getConnection(url, config.toProperties());

        this.init();

        LOG.log(Level.INFO, "Database is online");
    }

    private void init() {
        LOG.log(Level.INFO, "Creating tables");
        try {
            final PreparedStatement createIntents      = connection.prepareStatement(STMT_CREATE_TABLE_INTENTS);
            final PreparedStatement createApplications = connection.prepareStatement(STMT_CREATE_TABLE_APPLICATIONS);

            createIntents.execute();
            createApplications.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not create tables", e);
        }
    }

    @Override
    public @NotNull List<String> getIntents(long application) throws DatabaseException {
        ArrayList<String> intents = new ArrayList<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(STMT_SELECT_APPLICATION_INTENTS)) {
            stmt.setLong(1, application);

            ResultSet result = stmt.executeQuery();

            while (result.next())
                intents.add(result.getString("intent"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return List.copyOf(intents);
    }

    @Override
    public boolean hasIntent(long application, @NotNull String intent) throws DatabaseException {
        try (PreparedStatement stmt = this.connection.prepareStatement(STMT_SELECT_APPLICATION_INTENT)) {
            stmt.setLong(1, application);
            stmt.setString(2, intent);

            ResultSet result = stmt.executeQuery();

            result.next();
            int count = result.getInt(1);

            return count != 0;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addIntent(long application, @NotNull String intent) throws DatabaseException {
        try (PreparedStatement stmt = this.connection.prepareStatement(STMT_INSERT_INTENT)) {
            stmt.setString(1, intent);
            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = this.connection.prepareStatement(STMT_INSERT_APPLICATION_INTENT)) {
            stmt.setLong(1, application);
            stmt.setString(2, intent);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
