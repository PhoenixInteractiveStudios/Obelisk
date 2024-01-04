package org.burrow_studios.obelisk.server.db.dedicated.board;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

class SQLiteBoardDB extends SQLBoardDB {
    @Override
    protected @NotNull Set<Long> getBoardIds0() throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getBoard0(long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createBoard0(long id, @NotNull String title, long group) throws SQLException {
        // TODO
    }

    @Override
    protected void updateBoardTitle0(long id, @NotNull String title) throws SQLException {
        // TODO
    }

    @Override
    protected void updateBoardGroup0(long board, long group) throws SQLException {
        // TODO
    }

    // ISSUES

    @Override
    protected @NotNull Set<Long> getIssueIds0(long board) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getIssue0(long board, long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createIssue0(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws SQLException {
        // TODO
    }

    @Override
    protected void updateIssueTitle0(long board, long id, @NotNull String title) throws SQLException {
        // TODO
    }

    @Override
    protected void updateIssueState0(long board, long id, @NotNull IssueState state) throws SQLException {
        // TODO
    }

    @Override
    protected void addIssueAssignee0(long board, long id, long assignee) throws SQLException {
        // TODO
    }

    @Override
    protected void removeIssueAssignee0(long board, long id, long assignee) throws SQLException {
        // TODO
    }

    @Override
    protected void addIssueTag0(long board, long id, long tag) throws SQLException {
        // TODO
    }

    @Override
    protected void removeIssueTag0(long board, long id, long tag) throws SQLException {
        // TODO
    }

    // TAGS

    @Override
    protected @NotNull Set<Long> getTagIds0(long board) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected @NotNull JsonObject getTag0(long board, long id) throws SQLException {
        // TODO
        return null;
    }

    @Override
    protected void createTag0(long board, long id, @NotNull String name) throws SQLException {
        // TODO
    }

    @Override
    protected void updateTagTitle0(long board, long id, @NotNull String name) throws SQLException {
        // TODO
    }
}
