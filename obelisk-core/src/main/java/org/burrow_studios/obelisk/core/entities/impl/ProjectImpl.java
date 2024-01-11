package org.burrow_studios.obelisk.core.entities.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.action.DeleteAction;
import org.burrow_studios.obelisk.api.entities.Project;
import org.burrow_studios.obelisk.api.entities.User;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.action.DeleteActionImpl;
import org.burrow_studios.obelisk.core.entities.action.project.ProjectModifierImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.net.http.Route;
import org.jetbrains.annotations.NotNull;

public final class ProjectImpl extends TurtleImpl implements Project {
    private @NotNull String title;
    private @NotNull Timings timings;
    private @NotNull State state;
    private final @NotNull DelegatingTurtleCacheView<UserImpl> members;

    public ProjectImpl(
            @NotNull ObeliskImpl api,
            long id,
            @NotNull String title,
            @NotNull Timings timings,
            @NotNull State state,
            @NotNull DelegatingTurtleCacheView<UserImpl> members
    ) {
        super(api, id);
        this.title = title;
        this.timings = timings;
        this.state = state;
        this.members = members;
    }

    @Override
    public @NotNull JsonObject toJson() {
        JsonObject json = super.toJson();
        json.addProperty("title", title);

        JsonObject timingJson = new JsonObject();
        if (timings.release() != null)
            timingJson.addProperty("release", timings.release().toString());
        if (timings.apply() != null)
            timingJson.addProperty("apply", timings.apply().toString());
        if (timings.start() != null)
            timingJson.addProperty("start", timings.start().toString());
        if (timings.end() != null)
            timingJson.addProperty("end", timings.end().toString());
        json.add("timings", timingJson);

        json.addProperty("state", state.name());

        JsonArray memberIds = new JsonArray();
        for (long memberId : this.members.getIdsAsImmutaleSet())
            memberIds.add(memberId);
        json.add("members", memberIds);

        return json;
    }

    @Override
    public @NotNull ProjectModifierImpl modify() {
        return new ProjectModifierImpl(this);
    }

    @Override
    public @NotNull DeleteAction<Project> delete() {
        return new DeleteActionImpl<>(
                this.getAPI(),
                Project.class,
                this.getId(),
                Route.Project.DELETE.builder()
                        .withArg(getId())
                        .compile()
        );
    }

    @Override
    public @NotNull String getTitle() {
        return this.title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @Override
    public @NotNull Timings getTimings() {
        return this.timings;
    }

    public void setTimings(@NotNull Timings timings) {
        this.timings = timings;
    }

    @Override
    public @NotNull State getState() {
        return this.state;
    }

    public void setState(@NotNull State state) {
        this.state = state;
    }

    @Override
    public @NotNull DelegatingTurtleCacheView<UserImpl> getMembers() {
        return this.members;
    }

    @Override
    public @NotNull ActionImpl<Project> addMember(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Route.Project.ADD_MEMBER.builder()
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }

    @Override
    public @NotNull ActionImpl<Project> removeMember(@NotNull User user) {
        return new ActionImpl<>(this.api, this,
                Route.Project.DEL_MEMBER.builder()
                        .withArg(getId())
                        .withArg(user.getId())
                        .compile()
        );
    }
}
