package org.burrow_studios.obelisk.server.moderation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.entities.checks.ProjectChecks;
import org.burrow_studios.obelisk.core.entities.checks.TicketChecks;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.burrow_studios.obelisk.server.db.Cache;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.entity.ProjectDB;
import org.burrow_studios.obelisk.server.db.entity.TicketDB;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModerationService {
    private final ObeliskServer server;

    private final ProjectDB projectDB;
    private final  TicketDB  ticketDB;

    private final Cache<JsonObject> projectCache;
    private final Cache<JsonObject>  ticketCache;

    private final TurtleGenerator turtleGenerator;

    public ModerationService(@NotNull ObeliskServer server) {
        this.server = server;

        this.projectDB = server.getEntityProvider().getProjectDB();
        this.ticketDB  = server.getEntityProvider().getTicketDB();

        this.projectCache = new Cache<>();
        this.ticketCache  = new Cache<>();

        this.turtleGenerator = TurtleGenerator.get("ModerationService");

        this.projectDB.getProjectIds().forEach(projectCache::add);
        this.ticketDB.getTicketIds().forEach(ticketCache::add);
    }

    public @NotNull Set<Long> getProjects() {
        return this.projectCache.getIds();
    }

    public @NotNull Set<Long> getTickets() {
        return this.ticketCache.getIds();
    }

    public @NotNull JsonObject getProject(long id) throws DatabaseException {
        return this.projectCache.get(id)
                .orElseGet(() -> this.retrieveProject(id));
    }

    public @NotNull JsonObject getTicket(long id) throws DatabaseException {
        return this.ticketCache.get(id)
                .orElseGet(() -> this.retrieveTicket(id));
    }

    private @NotNull JsonObject retrieveProject(long id) throws DatabaseException {
        final JsonObject result = this.projectDB.getProject(id);
        this.projectCache.put(id, result);
        return result;
    }

    private @NotNull JsonObject retrieveTicket(long id) throws DatabaseException {
        final JsonObject result = this.ticketDB.getTicket(id);
        this.ticketCache.put(id, result);
        return result;
    }

    public @NotNull JsonObject createProject(@NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final ProjectState state = ProjectState.valueOf(stateStr);

        ProjectChecks.checkTitle(title);

        // TODO: timings

        this.projectDB.createProject(id, title, state);
        return this.retrieveProject(id);
    }

    public @NotNull JsonObject createTicket(@NotNull JsonObject json) throws DatabaseException {
        final long id = this.turtleGenerator.newId();

        final String title    = json.get("title").getAsString();
        final String stateStr = json.get("state").getAsString();

        final TicketState state = TicketState.valueOf(stateStr);

        TicketChecks.checkTitle(title);

        // TODO: tags?

        this.ticketDB.createTicket(id, title, state);
        return this.retrieveTicket(id);
    }

    public void patchProject(long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> {
                    ProjectChecks.checkTitle(title);
                    projectDB.updateProjectTitle(id, title);
                });

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(ProjectState::valueOf)
                .ifPresent(state -> projectDB.updateProjectState(id, state));

        JsonObject project = this.getProject(id);

        JsonObject timings = project.getAsJsonObject("timings");
        Optional.ofNullable(json.get("timings"))
                .map(JsonElement::getAsJsonObject)
                .ifPresent(elements -> {
                    for (Map.Entry<String, JsonElement> entry : elements.entrySet()) {
                        String name = entry.getKey();
                        String time = entry.getValue().getAsString();

                        if (!timings.has(name))
                            projectDB.addProjectTiming(id, name, Instant.parse(time));
                    }

                    for (String name : timings.keySet())
                        if (!elements.has(name))
                            projectDB.removeProjectTiming(id, name);
                });

        this.retrieveProject(id);
    }

    public void patchTicket(long id, @NotNull JsonObject json) throws DatabaseException {
        Optional.ofNullable(json.get("title"))
                .map(JsonElement::getAsString)
                .ifPresent(title -> {
                    TicketChecks.checkTitle(title);
                    ticketDB.updateTicketTitle(id, title);
                });

        Optional.ofNullable(json.get("state"))
                .map(JsonElement::getAsString)
                .map(TicketState::valueOf)
                .ifPresent(state -> ticketDB.updateTicketState(id, state));

        JsonObject ticket = this.getTicket(id);

        JsonArray tags = ticket.getAsJsonArray("tags");
        Optional.ofNullable(json.get("tags"))
                .map(JsonElement::getAsJsonArray)
                .ifPresent(elements -> {
                    // add new tags
                    for (JsonElement element : elements)
                        if (!tags.contains(element))
                            ticketDB.addTicketTag(id, element.getAsString());
                    // remove old tags
                    for (JsonElement element : tags)
                        if (!elements.contains(element))
                            ticketDB.removeTicketTag(id, element.getAsString());
                });

        this.retrieveTicket(id);
    }

    public void addProjectMember(long project, long user) throws DatabaseException {
        this.projectDB.addProjectMember(project, user);
        this.retrieveProject(project);
    }

    public void removeProjectMember(long project, long user) throws DatabaseException {
        this.projectDB.removeProjectMember(project, user);
        this.retrieveProject(project);
    }

    public void addTicketUser(long ticket, long user) throws DatabaseException {
        this.ticketDB.addTicketUser(ticket, user);
        this.retrieveProject(ticket);
    }

    public void removeTicketUser(long ticket, long user) throws DatabaseException {
        this.ticketDB.removeTicketUser(ticket, user);
        this.retrieveProject(ticket);
    }
}
