package org.burrow_studios.obelisk.server.its.db;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class MySQLBoardDB extends SQLBoardDB {
    private static final String URL = "jdbc:mysql://%s:%s/%s?allowMultiQueries=true";

    public MySQLBoardDB(@NotNull String host, int port, @NotNull String database, @NotNull String user, @NotNull String pass) throws DatabaseException {
        super(URL.formatted(host, port, database), user, pass, "board/tables_board");
    }

    @Override
    protected @NotNull Set<Long> getBoardIds0() throws SQLException {
        HashSet<Long> set = new HashSet<>();

        try (ResultSet result = executeQuery("board/get_boards")) {
            while (result.next())
                set.add(result.getLong(1));
        }

        return Set.copyOf(set);
    }

    @Override
    protected @NotNull JsonObject getBoard0(long id) throws SQLException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("board/get_board")) {
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
        try (PreparedStatement stmt = prepareStatement("board/create_board")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setLong(3, group);

            stmt.execute();
        }
    }

    @Override
    protected void updateBoardTitle0(long id, @NotNull String title) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/update_board_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateBoardGroup0(long board, long group) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/update_board_group")) {
            stmt.setLong(1, group);
            stmt.setLong(2, board);

            stmt.execute();
        }
    }

    // ISSUES

    @Override
    protected @NotNull Set<Long> getIssueIds0(long board) throws SQLException {
        HashSet<Long> set = new HashSet<>();

        try (ResultSet result = executeQuery("board/get_issues")) {
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

        try (PreparedStatement stmt = prepareStatement("board/get_issue")) {
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
        try (PreparedStatement stmt = prepareStatement("board/create_issue")) {
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
        try (PreparedStatement stmt = prepareStatement("board/update_issue_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        }
    }

    @Override
    protected void updateIssueState0(long board, long id, @NotNull IssueState state) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/update_issue_state")) {
            stmt.setString(1, state.name());
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        }
    }

    @Override
    protected void addIssueAssignee0(long board, long id, long assignee) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/add_issue_assignee")) {
            stmt.setLong(1, id);
            stmt.setLong(2, assignee);

            stmt.execute();
        }
    }

    @Override
    protected void removeIssueAssignee0(long board, long id, long assignee) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/remove_issue_assignee")) {
            stmt.setLong(1, id);
            stmt.setLong(2, assignee);

            stmt.execute();
        }
    }

    @Override
    protected void addIssueTag0(long board, long id, long tag) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/add_issue_tag")) {
            stmt.setLong(1, id);
            stmt.setLong(2, tag);

            stmt.execute();
        }
    }

    @Override
    protected void removeIssueTag0(long board, long id, long tag) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/remove_issue_tag")) {
            stmt.setLong(1, id);
            stmt.setLong(2, tag);

            stmt.execute();
        }
    }

    // TAGS

    @Override
    protected @NotNull Set<Long> getTagIds0(long board) throws SQLException {
        HashSet<Long> set = new HashSet<>();

        try (ResultSet result = executeQuery("board/get_tags")) {
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

        try (PreparedStatement stmt = prepareStatement("board/get_tag")) {
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
        try (PreparedStatement stmt = prepareStatement("board/create_tag")) {
            stmt.setLong(1, id);
            stmt.setLong(2, board);
            stmt.setString(3, name);

            stmt.execute();
        }
    }

    @Override
    protected void updateTagTitle0(long board, long id, @NotNull String name) throws SQLException {
        try (PreparedStatement stmt = prepareStatement("board/update_tag_name")) {
            stmt.setString(1, name);
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        }
    }
}
