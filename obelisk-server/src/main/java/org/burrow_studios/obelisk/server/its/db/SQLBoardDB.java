package org.burrow_studios.obelisk.server.its.db;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.SQLDB;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Set;

abstract class SQLBoardDB extends SQLDB implements BoardDB {
    @Override
    public final @NotNull Set<Long> getBoardIds() throws DatabaseException {
        return this.wrap(this::getBoardIds0);
    }

    @Override
    public final @NotNull JsonObject getBoard(long id) throws DatabaseException {
        return this.wrap(() -> this.getBoard0(id));
    }

    @Override
    public final void createBoard(long id, @NotNull String title, long group) throws DatabaseException {
        this.wrap(() -> this.createBoard0(id, title, group));
    }

    @Override
    public final void updateBoardTitle(long id, @NotNull String title) throws DatabaseException {
        this.wrap(() -> this.updateBoardTitle0(id, title));
    }

    @Override
    public final void updateBoardGroup(long board, long group) throws DatabaseException {
        this.wrap(() -> this.updateBoardGroup0(board, group));
    }

    protected abstract @NotNull Set<Long> getBoardIds0() throws SQLException;

    protected abstract @NotNull JsonObject getBoard0(long id) throws SQLException;

    protected abstract void createBoard0(long id, @NotNull String title, long group) throws SQLException;

    protected abstract void updateBoardTitle0(long id, @NotNull String title) throws SQLException;

    protected abstract void updateBoardGroup0(long board, long group) throws SQLException;

    // ISSUES

    @Override
    public final @NotNull Set<Long> getIssueIds(long board) throws DatabaseException {
        return this.wrap(() -> this.getIssueIds0(board));
    }

    @Override
    public final @NotNull JsonObject getIssue(long board, long id) throws DatabaseException {
        return this.wrap(() -> this.getIssue0(board, id));
    }

    @Override
    public final void createIssue(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws DatabaseException {
        this.wrap(() -> this.createIssue0(board, id, author, title, state));
    }

    @Override
    public final void updateIssueTitle(long board, long id, @NotNull String title) throws DatabaseException {
        this.wrap(() -> this.updateIssueTitle0(board, id, title));
    }

    @Override
    public final void updateIssueState(long board, long id, @NotNull IssueState state) throws DatabaseException {
        this.wrap(() -> this.updateIssueState0(board, id, state));
    }

    @Override
    public final void addIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        this.wrap(() -> this.addIssueAssignee0(board, id, assignee));
    }

    @Override
    public final void removeIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        this.wrap(() -> this.removeIssueAssignee0(board, id, assignee));
    }

    @Override
    public final void addIssueTag(long board, long id, long tag) throws DatabaseException {
        this.wrap(() -> this.addIssueTag0(board, id, tag));
    }

    @Override
    public final void removeIssueTag(long board, long id, long tag) throws DatabaseException {
        this.wrap(() -> this.removeIssueTag0(board, id, tag));
    }

    protected abstract @NotNull Set<Long> getIssueIds0(long board) throws SQLException;

    protected abstract @NotNull JsonObject getIssue0(long board, long id) throws SQLException;

    protected abstract void createIssue0(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws SQLException;

    protected abstract void updateIssueTitle0(long board, long id, @NotNull String title) throws SQLException;

    protected abstract void updateIssueState0(long board, long id, @NotNull IssueState state) throws SQLException;

    protected abstract void addIssueAssignee0(long board, long id, long assignee) throws SQLException;

    protected abstract void removeIssueAssignee0(long board, long id, long assignee) throws SQLException;

    protected abstract void addIssueTag0(long board, long id, long tag) throws SQLException;

    protected abstract void removeIssueTag0(long board, long id, long tag) throws SQLException;

    // TAGS

    @Override
    public final @NotNull Set<Long> getTagIds(long board) throws DatabaseException {
        return this.wrap(() -> this.getTagIds0(board));
    }

    @Override
    public final @NotNull JsonObject getTag(long board, long id) throws DatabaseException {
        return this.wrap(() -> this.getTag0(board, id));
    }

    @Override
    public final void createTag(long board, long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.createTag0(board, id, name));
    }

    @Override
    public final void updateTagTitle(long board, long id, @NotNull String name) throws DatabaseException {
        this.wrap(() -> this.updateTagTitle0(board, id, name));
    }

    protected abstract @NotNull Set<Long> getTagIds0(long board) throws SQLException;

    protected abstract @NotNull JsonObject getTag0(long board, long id) throws SQLException;

    protected abstract void createTag0(long board, long id, @NotNull String name) throws SQLException;

    protected abstract void updateTagTitle0(long board, long id, @NotNull String name) throws SQLException;
}
