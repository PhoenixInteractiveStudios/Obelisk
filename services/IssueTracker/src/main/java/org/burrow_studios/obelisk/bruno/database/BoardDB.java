package org.burrow_studios.obelisk.bruno.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.burrow_studios.obelisk.bruno.exceptions.DatabaseException;
import org.burrow_studios.obelisk.bruno.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BoardDB implements Database {
    private static final String URL = "mongodb+srv://%s:%s@%s:%s";

    private final MongoClient mongoClient;
    private final MongoCollection<Document> boards;
    private final MongoCollection<Document> issues;
    private final MongoCollection<Document> tags;

    public BoardDB(@NotNull String host, int port, @NotNull String user, @NotNull String pass, @NotNull String database) {
        this.mongoClient = MongoClients.create(URL.formatted(user, pass, host, port));
        MongoDatabase db = this.mongoClient.getDatabase(database);

        this.boards = db.getCollection("boards");
        this.issues = db.getCollection("issues");
        this.tags   = db.getCollection("tags");
    }

    @Override
    public void close() throws IOException {
        this.mongoClient.close();
    }

    @Override
    public @NotNull Set<Long> getBoardIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (MongoCursor<Document> cursor = this.boards.find().projection(new Document("_id", 1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ids.add(doc.getLong("_id"));
            }
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getBoard(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        Document doc = this.boards.find(
                eq("_id", id)
        ).first();

        if (doc == null)
            throw new NoSuchEntryException();

        json.addProperty("title", doc.getString("title"));
        json.addProperty("group", doc.getLong("group_id"));

        List<Long> issueList = doc.getList("issues", Long.class);
        JsonArray issueArr = new JsonArray(issueList.size());
        for (Long issue : issueList)
            issueArr.add(issue);
        json.add("issues", issueArr);

        List<Long> tagList = doc.getList("tags", Long.class);
        JsonArray tagArr = new JsonArray(tagList.size());
        for (Long tagId : tagList) {
            JsonObject tag = this.getTag(id, tagId);
            tag.remove("board");
            tagArr.add(tag);
        }
        json.add("assignees", tagArr);

        return json;
    }

    @Override
    public void createBoard(long id, @NotNull String title, long group) throws DatabaseException {
        Document doc = new Document();
        doc.put("_id", id);
        doc.put("title", title);
        doc.put("group", group);

        this.boards.insertOne(doc);
    }

    @Override
    public void updateBoardTitle(long id, @NotNull String title) throws DatabaseException {
        UpdateResult result = this.boards.updateOne(
                eq("_id", id),
                set("title", title)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void updateBoardGroup(long board, long group) throws DatabaseException {
        UpdateResult result = this.boards.updateOne(
                eq("_id", board),
                set("group", group)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void deleteBoard(long id) throws DatabaseException {
        DeleteResult result = this.boards.deleteOne(
                eq("_id", id)
        );
    }

    @Override
    public @NotNull Set<Long> getIssueIds(long board) throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (MongoCursor<Document> cursor = this.issues.find().projection(new Document("_id", 1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ids.add(doc.getLong("_id"));
            }
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getIssue(long board, long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        Document doc = this.issues.find(
                and(
                        eq("board", board),
                        eq("_id", id)
                )
        ).first();

        if (doc == null)
            throw new NoSuchEntryException();

        json.addProperty("board", doc.getLong("board"));
        json.addProperty("author", doc.getLong("author"));
        json.addProperty("title", doc.getString("title"));
        json.addProperty("state", doc.getString("state"));

        List<Long> assigneeList = doc.getList("assignees", Long.class);
        JsonArray assigneeArr = new JsonArray(assigneeList.size());
        for (Long assignee : assigneeList)
            assigneeArr.add(assignee);
        json.add("assignees", assigneeArr);

        List<Long> tagList = doc.getList("tags", Long.class);
        JsonArray tagArr = new JsonArray(tagList.size());
        for (Long tagId : tagList) {
            JsonObject tag = this.getTag(board, tagId);
            tag.remove("board");
            tagArr.add(tag);
        }
        json.add("assignees", tagArr);

        return json;
    }

    @Override
    public void createIssue(long board, long id, long author, @NotNull String title, @NotNull IssueState state) throws DatabaseException {
        Document doc = new Document();
        doc.put("_id", id);
        doc.put("board", board);
        doc.put("author", author);
        doc.put("title", title);
        doc.put("state", state.name());

        this.issues.insertOne(doc);
    }

    @Override
    public void updateIssueTitle(long board, long id, @NotNull String title) throws DatabaseException {
        UpdateResult result = this.issues.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("title", title)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void updateIssueState(long board, long id, @NotNull IssueState state) throws DatabaseException {
        UpdateResult result = this.issues.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("state", state.name())
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void addIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        UpdateResult result = this.issues.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("$addToSet", new Document("assignees", assignee))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void removeIssueAssignee(long board, long id, long assignee) throws DatabaseException {
        UpdateResult result = this.issues.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("$pull", new Document("assignees", assignee))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void addIssueTag(long board, long id, long tag) throws DatabaseException {
        // provoke a NoSuchElementException if the tag does not exist
        this.getTag(board, tag);

        UpdateResult result = this.issues.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("$addToSet", new Document("tags", tag))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void removeIssueTag(long board, long id, long tag) throws DatabaseException {
        // provoke a NoSuchElementException if the tag does not exist
        this.getTag(board, tag);

        UpdateResult result = this.issues.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("$pull", new Document("tags", tag))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void deleteIssue(long board, long id) throws DatabaseException {
        DeleteResult result = this.issues.deleteOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                )
        );
    }

    @Override
    public @NotNull Set<Long> getTagIds(long board) throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (MongoCursor<Document> cursor = this.tags.find().projection(new Document("_id", 1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ids.add(doc.getLong("_id"));
            }
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getTag(long board, long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        Document doc = this.issues.find(
                and(
                        eq("board", board),
                        eq("_id", id)
                )
        ).first();

        if (doc == null)
            throw new NoSuchEntryException();

        json.addProperty("board", doc.getLong("board"));
        json.addProperty("name", doc.getString("name"));

        return json;
    }

    @Override
    public void createTag(long board, long id, @NotNull String name) throws DatabaseException {
        Document doc = new Document();
        doc.put("_id", id);
        doc.put("board", board);
        doc.put("name", name);

        this.issues.insertOne(doc);
    }

    @Override
    public void updateTagTitle(long board, long id, @NotNull String name) throws DatabaseException {
        UpdateResult result = this.tags.updateOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                ),
                set("name", name)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void deleteTag(long board, long id) throws DatabaseException {
        DeleteResult result = this.tags.deleteOne(
                and(
                        eq("board", board),
                        eq("_id", id)
                )
        );
    }
}
