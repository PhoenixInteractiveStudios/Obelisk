package org.burrow_studios.obelisk.server.db.dedicated.board;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

abstract class SQLBoardDB implements BoardDB {
    @Override
    public final @NotNull Set<Long> getBoardIds() throws DatabaseException {
        try {
            return this.getBoardIds0();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getBoardIds0() throws SQLException;

    @Override
    public final @NotNull JsonObject getBoard(long id) throws DatabaseException {
        try {
            return this.getBoard0(id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getBoard0(long id) throws SQLException;

    @Override
    public final void createBoard(long id, @NotNull String title, long group) throws DatabaseException {
        try {
            this.createBoard0(id, title, group);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createBoard0(long id, @NotNull String title, long group) throws SQLException;

    @Override
    public final void updateBoardTitle(long id, @NotNull String title) throws DatabaseException {
        try {
            this.updateBoardTitle0(id, title);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateBoardTitle0(long id, @NotNull String title) throws SQLException;

    @Override
    public final void updateBoardGroup(long board, long group) throws DatabaseException {
        try {
            this.updateBoardGroup0(board, group);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateBoardGroup0(long board, long group) throws SQLException;

    // ISSUES

    @Override
    public final @NotNull Set<Long> getIssueIds(long board) throws DatabaseException {
        try {
            return this.getIssueIds0(board);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getIssueIds0(long board) throws SQLException;

    @Override
    public final @NotNull JsonObject getIssue(long board, long id) throws DatabaseException {
        try {
            return this.getIssue0(board, id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getIssue0(long board, long id) throws SQLException;

    @Override
    public final void createIssue(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws DatabaseException {
        try {
            this.createIssue0(board, id, author, title, state);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createIssue0(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws SQLException;

    @Override
    public final void updateIssueTitle(long board, long id, @NotNull String title) throws DatabaseException {
        try {
            this.updateIssueTitle0(board, id, title);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateIssueTitle0(long board, long id, @NotNull String title) throws SQLException;

    @Override
    public final void updateIssueState(long board, long id, @NotNull IssueState state) throws DatabaseException {
        try {
            this.updateIssueState0(board, id, state);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateIssueState0(long board, long id, @NotNull IssueState state) throws SQLException;

    @Override
    public final void addIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        try {
            this.addIssueAssignee0(board, id, assignee);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void addIssueAssignee0(long board, long id, long assignee) throws SQLException;

    @Override
    public final void removeIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        try {
            this.removeIssueAssignee0(board, id, assignee);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeIssueAssignee0(long board, long id, long assignee) throws SQLException;

    @Override
    public final void addIssueTag(long board, long id, long tag) throws DatabaseException {
        try {
            this.addIssueTag0(board, id, tag);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void addIssueTag0(long board, long id, long tag) throws SQLException;

    @Override
    public final void removeIssueTag(long board, long id, long tag) throws DatabaseException {
        try {
            this.removeIssueTag0(board, id, tag);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void removeIssueTag0(long board, long id, long tag) throws SQLException;

    // TAGS

    @Override
    public final @NotNull Set<Long> getTagIds(long board) throws DatabaseException {
        try {
            return this.getTagIds0(board);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull Set<Long> getTagIds0(long board) throws SQLException;

    @Override
    public final @NotNull JsonObject getTag(long board, long id) throws DatabaseException {
        try {
            return this.getTag0(board, id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract @NotNull JsonObject getTag0(long board, long id) throws SQLException;

    @Override
    public final void createTag(long board, long id, @NotNull String name) throws DatabaseException {
        try {
            this.createTag0(board, id, name);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createTag0(long board, long id, @NotNull String name) throws SQLException;

    @Override
    public final void updateTagTitle(long board, long id, @NotNull String name) throws DatabaseException {
        try {
            this.updateTagTitle0(board, id, name);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void updateTagTitle0(long board, long id, @NotNull String name) throws SQLException;
}
