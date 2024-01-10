package org.burrow_studios.obelisk.server.its.db;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

class MySQLBoardDB extends SQLBoardDB {
    private static final String URL = "jdbc:mysql://{0}:{1}/{2}";

    private static final String CREATE_TABLE_BOARDS = "CREATE TABLE IF NOT EXISTS `boards` (`id` BIGINT(20) NOT NULL, `title` TEXT NOT NULL, `group_id` BIGINT(20) NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_ISSUES = "CREATE TABLE IF NOT EXISTS `issues` (`id` BIGINT(20) NOT NULL, `board` BIGINT(20) NOT NULL, `author` BIGINT(20) NOT NULL, `title` TEXT NOT NULL, `state` TEXT NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_TAGS   = "CREATE TABLE IF NOT EXISTS `tags` (`id` BIGINT(20) NOT NULL, `board` BIGINT(20) NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY (`id`));";
    private static final String CREATE_TABLE_ISSUE_ASSIGNEES = "CREATE TABLE IF NOT EXISTS `issue_assignees` (`issue` BIGINT(20) NOT NULL, `user` BIGINT(20) NOT NULL, PRIMARY KEY (`issue`, `user`));";
    private static final String CREATE_TABLE_ISSUE_TAGS      = "CREATE TABLE IF NOT EXISTS `issue_tags` (`issue` BIGINT(20) NOT NULL, `tag` BIGINT(20) NOT NULL, PRIMARY KEY (`issue`, `tag`));";
    private static final String ALTER_TABLE_ISSUES = "ALTER TABLE `issues` ADD FOREIGN KEY (`board`) REFERENCES `boards`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";
    private static final String ALTER_TABLE_TAGS   = "ALTER TABLE `tags` ADD FOREIGN KEY (`board`) REFERENCES `boards`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";
    private static final String ALTER_TABLE_ISSUE_TAGS1 = "ALTER TABLE `issue_tags` ADD FOREIGN KEY (`issue`) REFERENCES `issues`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";
    private static final String ALTER_TABLE_ISSUE_TAGS2 = "ALTER TABLE `issue_tags` ADD FOREIGN KEY (`tag`) REFERENCES `tags`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;";

    private static final String GET_BOARD_IDS = "SELECT `id` FROM `boards`;";
    private static final String GET_ISSUE_IDS = "SELECT `id` FROM `issues` WHERE `board` = ?;";
    private static final String GET_TAG_IDS   = "SELECT `id` FROM `tags` WHERE `board` = ?;";

    private static final String GET_BOARD = "SELECT * FROM `boards` WHERE `id` = ?;";
    private static final String GET_ISSUE = "SELECT * FROM `issues` WHERE `board` = ? AND `id` = ?;";
    private static final String GET_TAG   = "SELECT * FROM `tags` WHERE `board` = ? AND `id` = ?;";

    private static final String CREATE_BOARD = "INSERT INTO `boards` (`id`, `title`, `group_id`) VALUES ('?', '?', '?');";
    private static final String CREATE_ISSUE = "INSERT INTO `issues` (`id`, `board`, `author`, `title`, `state`) VALUES ('?', '?', '?', '?', '?');";
    private static final String CREATE_TAG   = "INSERT INTO `tags` (`id`, `board`, `name`) VALUES ('?', '?', '?')";

    private static final String UPDATE_BOARD_TITLE = "UPDATE `boards` SET `title` = '?' WHERE `id` = ?;";
    private static final String UPDATE_BOARD_GROUP = "UPDATE `boards` SET `group_id` = ? WHERE `id` = ?;";
    private static final String UPDATE_ISSUE_TITLE = "UPDATE `issues` SET `title` = '?' WHERE `board` = ? AND `id` = ?;";
    private static final String UPDATE_ISSUE_STATE = "UPDATE `issues` SET `state` = '?' WHERE `board` = ? AND `id` = ?;";
    private static final String UPDATE_TAG_NAME = "UPDATE `tags` SET `name` = '?' WHERE `board` = ? AND `id` = ?;";

    private static final String ADD_ISSUE_ASSIGNEE = "INSERT INTO `issue_assignees` (`issue`, `user`) VALUES ('?', '?');";
    private static final String ADD_ISSUE_TAG      = "INSERT INTO `issue_tags` (`issue`, `tag`) VALUES ('?', '?');";

    private static final String REMOVE_ISSUE_ASSIGNEE = "DELETE FROM `issue_assignees` WHERE `issue` = ? AND `user` = ?;";
    private static final String REMOVE_ISSUE_TAG      = "DELETE FROM `issue_tags` WHERE `issue` = ? AND `tag` = ?;";

    private final Connection connection;

    public MySQLBoardDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        final String url = MessageFormat.format(URL, host, port, database);

        try {
            this.connection = DriverManager.getConnection(url, user, pass);

            final PreparedStatement createBoards = this.connection.prepareStatement(CREATE_TABLE_BOARDS);
            final PreparedStatement createIssues = this.connection.prepareStatement(CREATE_TABLE_ISSUES);
            final PreparedStatement createTags   = this.connection.prepareStatement(CREATE_TABLE_TAGS);
            final PreparedStatement createIssueAssignees = this.connection.prepareStatement(CREATE_TABLE_ISSUE_ASSIGNEES);
            final PreparedStatement createIssueTags      = this.connection.prepareStatement(CREATE_TABLE_ISSUE_TAGS);

            final PreparedStatement alterIssues = this.connection.prepareStatement(ALTER_TABLE_ISSUES);
            final PreparedStatement alterTags   = this.connection.prepareStatement(ALTER_TABLE_TAGS);
            final PreparedStatement alterIssueTags1 = this.connection.prepareStatement(ALTER_TABLE_ISSUE_TAGS1);
            final PreparedStatement alterIssueTags2 = this.connection.prepareStatement(ALTER_TABLE_ISSUE_TAGS2);

            createBoards.execute();
            createIssues.execute();
            createTags.execute();
            createIssueAssignees.execute();
            createIssueTags.execute();

            alterIssues.execute();
            alterTags.execute();
            alterIssueTags1.execute();
            alterIssueTags2.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected @NotNull Set<Long> getBoardIds0() throws SQLException {
        HashSet<Long> set = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_BOARD_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                set.add(result.getLong(1));
        }

        return Set.copyOf(set);
    }

    @Override
    protected @NotNull JsonObject getBoard0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_BOARD)) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("group", result.getLong("group_id"));
        }

        return json;
    }

    @Override
    protected void createBoard0(long id, @NotNull String title, long group) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_BOARD)) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setLong(3, group);

            stmt.execute();
        }
    }

    @Override
    protected void updateBoardTitle0(long id, @NotNull String title) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_BOARD_TITLE)) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateBoardGroup0(long board, long group) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_BOARD_GROUP)) {
            stmt.setLong(1, group);
            stmt.setLong(2, board);

            stmt.execute();
        }
    }

    // ISSUES

    @Override
    protected @NotNull Set<Long> getIssueIds0(long board) throws SQLException {
        HashSet<Long> set = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_ISSUE_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                set.add(result.getLong(1));
        }

        return Set.copyOf(set);
    }

    @Override
    protected @NotNull JsonObject getIssue0(long board, long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("board", board);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_ISSUE)) {
            stmt.setLong(1, board);
            stmt.setLong(2, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("author", result.getLong("author"));
            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        }

        // TODO: assignees and tags

        return json;
    }

    @Override
    protected void createIssue0(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_ISSUE)) {
            stmt.setLong(1, id);
            stmt.setLong(2, board);
            stmt.setLong(3, author);
            stmt.setString(4, title);
            stmt.setString(5, state.name());

            stmt.execute();
        }
    }

    @Override
    protected void updateIssueTitle0(long board, long id, @NotNull String title) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_ISSUE_TITLE)) {
            stmt.setString(1, title);
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateIssueState0(long board, long id, @NotNull IssueState state) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_ISSUE_STATE)) {
            stmt.setString(1, state.name());
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        }
    }

    @Override
    protected void addIssueAssignee0(long board, long id, long assignee) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_ISSUE_ASSIGNEE)) {
            stmt.setLong(1, id);
            stmt.setLong(2, assignee);

            stmt.execute();
        }
    }

    @Override
    protected void removeIssueAssignee0(long board, long id, long assignee) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_ISSUE_ASSIGNEE)) {
            stmt.setLong(1, id);
            stmt.setLong(2, assignee);

            stmt.execute();
        }
    }

    @Override
    protected void addIssueTag0(long board, long id, long tag) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(ADD_ISSUE_TAG)) {
            stmt.setLong(1, id);
            stmt.setLong(2, tag);

            stmt.execute();
        }
    }

    @Override
    protected void removeIssueTag0(long board, long id, long tag) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(REMOVE_ISSUE_TAG)) {
            stmt.setLong(1, id);
            stmt.setLong(2, tag);

            stmt.execute();
        }
    }

    // TAGS

    @Override
    protected @NotNull Set<Long> getTagIds0(long board) throws SQLException {
        HashSet<Long> set = new HashSet<>();

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_TAG_IDS)) {
            ResultSet result = stmt.executeQuery();

            while (result.next())
                set.add(result.getLong(1));
        }

        return Set.copyOf(set);
    }

    @Override
    protected @NotNull JsonObject getTag0(long board, long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("board", board);

        try (PreparedStatement stmt = this.connection.prepareStatement(GET_TAG)) {
            stmt.setLong(1, board);
            stmt.setLong(2, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("name", result.getString("name"));
        }

        return json;
    }

    @Override
    protected void createTag0(long board, long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(CREATE_TAG)) {
            stmt.setLong(1, id);
            stmt.setLong(2, board);
            stmt.setString(3, name);

            stmt.execute();
        }
    }

    @Override
    protected void updateTagTitle0(long board, long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = this.connection.prepareStatement(UPDATE_TAG_NAME)) {
            stmt.setString(1, name);
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        }
    }
}
