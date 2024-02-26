package org.burrow_studios.obelisk.moderationservice.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.burrow_studios.obelisk.moderationservice.exceptions.DatabaseException;
import org.burrow_studios.obelisk.moderationservice.exceptions.NoSuchEntryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public final class Database implements Closeable, ProjectDB, TicketDB {
    private static final String URL = "mongodb+srv://%s:%s@%s:%s";

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    private final MongoClient mongoClient;
    private final MongoCollection<Document> projects;
    private final MongoCollection<Document> tickets;

    public Database(@NotNull String host, int port, @NotNull String user, @NotNull String pass, @NotNull String database) {
        this.mongoClient = MongoClients.create(URL.formatted(user, pass, host, port));
        MongoDatabase db = this.mongoClient.getDatabase(database);

        this.projects = db.getCollection("projects");
        this.tickets  = db.getCollection("tickets");
    }

    @Override
    public void close() throws IOException {
        this.mongoClient.close();
    }

    @Override
    public @NotNull Set<Long> getProjectIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (MongoCursor<Document> cursor = this.projects.find().projection(new Document("_id", 1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ids.add(doc.getLong("_id"));
            }
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getProject(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        Document doc = this.projects.find(
                eq("_id", id)
        ).first();

        if (doc == null)
            throw new NoSuchEntryException();

        json.addProperty("title", doc.getString("title"));
        json.addProperty("state", doc.getString("state"));

        String timingStr = doc.getString("timings");
        JsonObject timings = gson.fromJson(timingStr, JsonObject.class);
        json.add("timings", timings);

        List<Long> memberIds = doc.getList("members", Long.class);
        JsonArray memberArr = new JsonArray(memberIds.size());
        for (Long member : memberIds)
            memberArr.add(member);
        json.add("members", memberArr);

        return json;
    }

    @Override
    public void createProject(long id, @NotNull String title, @NotNull ProjectState state) throws DatabaseException {
        Document doc = new Document();
        doc.put("_id", id);
        doc.put("title", title);
        doc.put("state", state.name());

        this.projects.insertOne(doc);
    }

    @Override
    public void updateProjectTitle(long id, @NotNull String title) throws DatabaseException {
        UpdateResult result = this.projects.updateOne(
                eq("_id", id),
                set("title", title)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    // TODO: synchronize this
    @Override
    public void addProjectTiming(long id, @NotNull String name, @NotNull Instant time) throws DatabaseException {
        JsonObject project = this.getProject(id);

        JsonObject timings = project.getAsJsonObject("timings");
        timings.addProperty(name, time.toString());

        String timingsString = gson.toJson(timings);

        UpdateResult result = this.projects.updateOne(
                eq("_id", id),
                set("timings", timingsString)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    // TODO: synchronize this
    @Override
    public void removeProjectTiming(long id, @NotNull String name) throws DatabaseException {
        JsonObject project = this.getProject(id);

        JsonObject timings = project.getAsJsonObject("timings");
        timings.remove(name);

        String timingsString = gson.toJson(timings);

        UpdateResult result = this.projects.updateOne(
                eq("_id", id),
                set("timings", timingsString)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void updateProjectState(long id, @NotNull ProjectState state) throws DatabaseException {
        UpdateResult result = this.projects.updateOne(
                eq("_id", id),
                set("state", state.name())
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void addProjectMember(long project, long user) throws DatabaseException {
        UpdateResult result = this.projects.updateOne(
                eq("_id", project),
                set("$addToSet", new Document("members", user))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void removeProjectMember(long project, long user) throws DatabaseException {
        UpdateResult result = this.projects.updateOne(
                eq("_id", project),
                set("$pull", new Document("members", user))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void deleteProject(long id) throws DatabaseException {
        DeleteResult result = this.projects.deleteOne(
                eq("_id", id)
        );
    }

    @Override
    public @NotNull Set<Long> getTicketIds() throws DatabaseException {
        HashSet<Long> ids = new HashSet<>();

        try (MongoCursor<Document> cursor = this.tickets.find().projection(new Document("_id", 1)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ids.add(doc.getLong("_id"));
            }
        }

        return Set.copyOf(ids);
    }

    @Override
    public @NotNull JsonObject getTicket(long id) throws DatabaseException {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        Document doc = this.tickets.find(
                eq("_id", id)
        ).first();

        if (doc == null)
            throw new NoSuchEntryException();

        json.addProperty("title", doc.getString("title"));
        json.addProperty("state", doc.getString("state"));

        List<Long> userIds = doc.getList("users", Long.class);
        JsonArray userArr = new JsonArray(userIds.size());
        for (Long user : userIds)
            userArr.add(user);
        json.add("users", userArr);

        List<String> tagList = doc.getList("tags", String.class);
        JsonArray tagArr = new JsonArray(tagList.size());
        for (String tag : tagList)
            tagArr.add(tag);
        json.add("tags", tagArr);

        return json;
    }

    @Override
    public void createTicket(long id, @Nullable String title, @NotNull TicketState state) throws DatabaseException {
        Document doc = new Document();
        doc.put("_id", id);
        doc.put("title", title);
        doc.put("state", state.name());

        this.tickets.insertOne(doc);
    }

    @Override
    public void updateTicketTitle(long id, @Nullable String title) throws DatabaseException {
        UpdateResult result = this.tickets.updateOne(
                eq("_id", id),
                set("title", title)
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void updateTicketState(long id, @NotNull TicketState state) throws DatabaseException {
        UpdateResult result = this.projects.updateOne(
                eq("_id", id),
                set("state", state.name())
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void addTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        UpdateResult result = this.tickets.updateOne(
                eq("_id", ticket),
                set("$addToSet", new Document("tags", tag))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void removeTicketTag(long ticket, @NotNull String tag) throws DatabaseException {
        UpdateResult result = this.tickets.updateOne(
                eq("_id", ticket),
                set("$pull", new Document("tags", tag))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void addTicketUser(long ticket, long user) throws DatabaseException {
        UpdateResult result = this.tickets.updateOne(
                eq("_id", ticket),
                set("$addToSet", new Document("users", user))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void removeTicketUser(long ticket, long user) throws DatabaseException {
        UpdateResult result = this.tickets.updateOne(
                eq("_id", ticket),
                set("$pull", new Document("users", user))
        );

        if (result.getMatchedCount() == 0)
            throw new NoSuchEntryException();
    }

    @Override
    public void deleteTicket(long id) throws DatabaseException {
        DeleteResult result = this.tickets.deleteOne(
                eq("_id", id)
        );
    }
}
