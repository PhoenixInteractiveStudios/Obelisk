package org.burrow_studios.obelisk.server.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.Ticket;
import org.burrow_studios.obelisk.api.entities.board.Issue;
import org.burrow_studios.obelisk.server.Main;
import org.burrow_studios.obelisk.server.db.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class EntityDatabase implements GroupDB, ProjectDB, TicketDB, UserDB, BoardDB {
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

    // PROJECTS

    @Override
    public @NotNull Set<Long> getProjectIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("project/get_projects")) {
            while (result.next())
                ids.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getProject(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("project/get_project")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("project/get_project_timings")) {
            stmt.setLong(1, id);

            JsonObject timings = new JsonObject();

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                final String  name = result.getString("name");
                final Instant time = result.getTimestamp("time").toInstant();

                timings.addProperty(name, time.toString());
            }

            json.add("timings", timings);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("project/get_project_members")) {
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
    public void createProject(long id, @NotNull String title, @NotNull Project.State state) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/create_project")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateProjectTitle(long id, @NotNull String title) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/update_project_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addProjectTiming(long id, @NotNull String name, @NotNull Instant time) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/add_project_timing")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setTimestamp(3, Timestamp.from(time));

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeProjectTiming(long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/remove_project_timing")) {
            stmt.setLong(1, id);
            stmt.setString(2, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateProjectState(long id, @NotNull Project.State state) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/update_project_state")) {
            stmt.setString(1, state.name());
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addProjectMember(long project, long user) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/add_project_member")) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeProjectMember(long project, long user) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("project/remove_project_member")) {
            stmt.setLong(1, project);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteProject(long id) throws DatabaseException {
        // TODO
    }

    // TICKETS

    @Override
    public @NotNull Set<Long> getTicketIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (ResultSet result = executeQuery("ticket/get_tickets")) {
            while (result.next())
                ids.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getTicket(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("ticket/get_ticket")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("state", result.getString("state"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("ticket/get_ticket_tags")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getString("tag"));

            json.add("tags", arr);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (PreparedStatement stmt = prepareStatement("ticket/get_ticket_users")) {
            stmt.setLong(1, id);

            JsonArray arr = new JsonArray();

            ResultSet result = stmt.executeQuery();

            while (result.next())
                arr.add(result.getLong("user"));

            json.add("users", arr);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return json;
    }

    @Override
    public void createTicket(long id, @Nullable String title, @NotNull Ticket.State state) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/create_ticket")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setString(3, state.name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateTicketTitle(long id, @Nullable String title) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/update_ticket_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateTicketState(long id, @NotNull Ticket.State state) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/update_ticket_state")) {
            stmt.setString(1, state.name());
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/add_ticket_tag")) {
            stmt.setLong(1, ticket);
            stmt.setString(2, tag);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/remove_ticket_tag")) {
            stmt.setLong(1, ticket);
            stmt.setString(2, tag);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addTicketUser(long ticket, long user) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/add_ticket_user")) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeTicketUser(long ticket, long user) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("ticket/remove_ticket_user")) {
            stmt.setLong(1, ticket);
            stmt.setLong(2, user);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteTicket(long id) throws DatabaseException {
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

    // BOARDS

    @Override
    public @NotNull Set<Long> getBoardIds() throws DatabaseException {
        HashSet<Long> set = new HashSet<>();

        try (ResultSet result = executeQuery("board/get_boards")) {
            while (result.next())
                set.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(set);
    }

    @Override
    public @NotNull JsonObject getBoard(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        try (PreparedStatement stmt = prepareStatement("board/get_board")) {
            stmt.setLong(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next())
                throw new NoSuchEntryException();

            json.addProperty("title", result.getString("title"));
            json.addProperty("group", result.getLong("group_id"));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return json;
    }

    @Override
    public void createBoard(long id, @NotNull String title, long group) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/create_board")) {
            stmt.setLong(1, id);
            stmt.setString(2, title);
            stmt.setLong(3, group);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateBoardTitle(long id, @NotNull String title) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/update_board_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateBoardGroup(long board, long group) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/update_board_group")) {
            stmt.setLong(1, group);
            stmt.setLong(2, board);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteBoard(long id) throws DatabaseException {
        // TODO
    }

    // ISSUES

    @Override
    public @NotNull Set<Long> getIssueIds(long board) throws DatabaseException {
        HashSet<Long> set = new HashSet<>();

        try (ResultSet result = executeQuery("board/get_issues")) {
            while (result.next())
                set.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(set);
    }

    @Override
    public @NotNull JsonObject getIssue(long board, long id) throws DatabaseException {
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
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        // TODO: assignees and tags

        return json;
    }

    @Override
    public void createIssue(long board, long id, long author, @NotNull String title, @NotNull Issue.State state) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/create_issue")) {
            stmt.setLong(1, id);
            stmt.setLong(2, board);
            stmt.setLong(3, author);
            stmt.setString(4, title);
            stmt.setString(5, state.name());

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateIssueTitle(long board, long id, @NotNull String title) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/update_issue_title")) {
            stmt.setString(1, title);
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateIssueState(long board, long id, @NotNull Issue.State state) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/update_issue_state")) {
            stmt.setString(1, state.name());
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/add_issue_assignee")) {
            stmt.setLong(1, id);
            stmt.setLong(2, assignee);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/remove_issue_assignee")) {
            stmt.setLong(1, id);
            stmt.setLong(2, assignee);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addIssueTag(long board, long id, long tag) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/add_issue_tag")) {
            stmt.setLong(1, id);
            stmt.setLong(2, tag);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeIssueTag(long board, long id, long tag) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/remove_issue_tag")) {
            stmt.setLong(1, id);
            stmt.setLong(2, tag);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteIssue(long board, long is) throws DatabaseException {
        // TODO
    }

    // TAGS

    @Override
    public @NotNull Set<Long> getTagIds(long board) throws DatabaseException {
        HashSet<Long> set = new HashSet<>();

        try (ResultSet result = executeQuery("board/get_tags")) {
            while (result.next())
                set.add(result.getLong(1));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Set.copyOf(set);
    }

    @Override
    public @NotNull JsonObject getTag(long board, long id) throws DatabaseException {
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
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return json;
    }

    @Override
    public void createTag(long board, long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/create_tag")) {
            stmt.setLong(1, id);
            stmt.setLong(2, board);
            stmt.setString(3, name);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateTagTitle(long board, long id, @NotNull String name) throws DatabaseException {
        try (PreparedStatement stmt = prepareStatement("board/update_tag_name")) {
            stmt.setString(1, name);
            stmt.setLong(2, board);
            stmt.setLong(3, id);

            stmt.execute();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteTag(long board, long id) throws DatabaseException {
        // TODO
    }
}
